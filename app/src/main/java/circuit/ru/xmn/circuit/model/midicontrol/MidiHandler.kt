package circuit.ru.xmn.circuit.model.midicontrol

class MidiHandler(
        val name: String,
        val channel: Int,
        val controlNumber: Int,
        val defaultValue: Int = 0,
        val controlType: MidiControlType = MidiControlType.CC) {
    private val byteBuffer = ByteArray(3)

    fun createMidiCommand(byteBuffer: ByteArray, value: Int) = controlType.createMidiCommand(byteBuffer, channel, controlNumber, value)

}