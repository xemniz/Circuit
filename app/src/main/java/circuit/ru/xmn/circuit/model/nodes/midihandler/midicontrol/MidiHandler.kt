package circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol

import kotlin.properties.Delegates

data class MidiHandler(
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

    var value by Delegates.observable(defaultValue) { _, _, value ->
        currentValue = value
        sendMessage?.let { send ->
            byteBuffer?.let { buf ->
                sendMidiCommand(send, buf, value)
            }
        }
    }

    private fun sendMidiCommand(send: (ByteArray) -> Unit, buf: ByteArray, value: Int) {
        controlType.sendMidiCommand(send, buf, channel - 1, controlNumber, value)
    }
}

data class Range(val lower: Int, val upper: Int)

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