package circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol

import circuit.ru.xmn.circuit.midiservice.MidiConstants

enum class MidiControlType {
    CC {
        override fun sendMidiCommand(send: (ByteArray) -> Unit, byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int) {
            send(byteBuffer.midi(channel, controlNumber, value))
        }
    },

    NRPN {
        override fun sendMidiCommand(send: (ByteArray) -> Unit, byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int) {
            send(byteBuffer.midi(channel, 99, NrpnValue.from(controlNumber).msb))
            send(byteBuffer.midi(channel, 98, NrpnValue.from(controlNumber).lsb))
            send(byteBuffer.midi(channel, 6, value))
        }
    };

    abstract fun sendMidiCommand(send: (ByteArray) -> Unit, byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int)
}

fun ByteArray.midi(channel: Int, controlNumber: Int, value: Int) = this.apply {
    this[0] = (MidiConstants.STATUS_CONTROL_CHANGE + channel).toByte()
    this[1] = controlNumber.toByte()
    this[2] = value.toByte()
}
