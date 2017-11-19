package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler

class SynthMidiController(
        val name: String,
        val controls: List<MidiHandler>,
        val sendMessage: (ByteArray) -> Unit = {},
        val buffer: ByteArray = ByteArray(3)) {

    fun control(name: String): MidiHandler {
        return controls
                .first { it.name == name }
                .apply {
                    byteBuffer = buffer
                    sendMessage = this@SynthMidiController.sendMessage
                }
    }
}