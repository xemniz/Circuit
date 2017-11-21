package circuit.ru.xmn.circuit.model.midicontrol

import java.util.*
import kotlin.properties.Delegates

class MidiHandler(
        val name: String,
        val channel: Int,
        val controlNumber: Int,
        val controlType: MidiControlType = MidiControlType.CC,
        val range: Range = Range(0, 127),
        val offset: Int = 0,
        val defaultValue: Int = 0
) {
    var byteBuffer: ByteArray? = null
    var sendMessage: ((ByteArray) -> Unit)? = null
    var currentValue = defaultValue

    private val changeListeners = WeakHashMap<(Int) -> Unit, Any>()
    private val VALUE_KEY_FOR_WEAK_MAP = Any()

    var value by Delegates.observable(defaultValue) { _, _, value ->
        changeListeners.keys.forEach { it(value) }
        sendMessage?.let { send ->
            byteBuffer?.let { buf ->
                sendMidiCommand(send, buf, value)
            }
        }
    }

    private fun sendMidiCommand(send: (ByteArray) -> Unit, buf: ByteArray, value: Int) {
        controlType.sendMidiCommand(send, buf,channel-1, controlNumber, value)
    }

    fun addListener(listener: (Int) -> Unit) {
        changeListeners.put(listener, VALUE_KEY_FOR_WEAK_MAP)
    }

}

class Range(val lower: Int, val upper: Int)

class NrpnValue(val msb: Int, val lsb: Int) {
    fun value() = (msb shl 7) + lsb

    companion object {
        fun from(value: Int): NrpnValue {
            val msb = (value / 128.0f).toInt()
            val lsb = (value - msb * 128.0f).toInt()
            return NrpnValue(msb, lsb)
        }
    }
}