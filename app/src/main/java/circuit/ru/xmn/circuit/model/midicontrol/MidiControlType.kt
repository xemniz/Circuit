package circuit.ru.xmn.circuit.model.midicontrol

import circuit.ru.xmn.circuit.midiservice.MidiConstants

enum class MidiControlType {
    CC {
        override fun createMidiCommand(byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int): ByteArray {
            byteBuffer[0] = (MidiConstants.STATUS_CONTROL_CHANGE + channel).toByte()
            byteBuffer[1] = controlNumber.toByte()
            byteBuffer[2] = value.toByte()

            return byteBuffer
        }
    },

    NRPN {
        override fun createMidiCommand(byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int): ByteArray {
            byteBuffer[0] = (MidiConstants.STATUS_CONTROL_CHANGE + channel).toByte()
            byteBuffer[1] = 0x63.toByte()
            byteBuffer[2] = NrpnValue.from(controlNumber).msb.toByte()

            byteBuffer[0] = (MidiConstants.STATUS_CONTROL_CHANGE + channel).toByte()
            byteBuffer[1] = 0x62.toByte()
            byteBuffer[2] = NrpnValue.from(controlNumber).lsb.toByte()

            byteBuffer[0] = (MidiConstants.STATUS_CONTROL_CHANGE + channel).toByte()
            byteBuffer[1] = 0x06.toByte()
            byteBuffer[2] = value.toByte()

            return byteBuffer
        }
    };

    abstract fun createMidiCommand(byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int): ByteArray
}