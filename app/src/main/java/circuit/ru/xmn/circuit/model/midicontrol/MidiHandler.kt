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

    private val changeListeners = WeakHashMap<(Int) -> Unit, Any>()
    private val nomatter = Any()
    var value by Delegates.observable(defaultValue) { _, _, value ->
        changeListeners.keys.forEach { it(value) }
        sendMessage?.let { send ->
            byteBuffer?.let { buf ->
                send(createMidiCommand(buf, value))
            }
        }
    }

    fun addListener(listener: (Int) -> Unit) {
        changeListeners.put(listener, nomatter)
    }

    fun createMidiCommand(byteBuffer: ByteArray, value: Int) = controlType.createMidiCommand(byteBuffer, channel - 1, controlNumber, value)

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