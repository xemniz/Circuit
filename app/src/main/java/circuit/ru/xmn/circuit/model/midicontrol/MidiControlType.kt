package circuit.ru.xmn.circuit.model.midicontrol

import circuit.ru.xmn.circuit.MidiConstants

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
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    };

    abstract fun createMidiCommand(byteBuffer: ByteArray, channel: Int, controlNumber: Int, value: Int): ByteArray
}