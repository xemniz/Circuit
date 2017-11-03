package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler

class SynthMidiController(
        private val controls: List<MidiHandler>,
        val sendMessage: (ByteArray) -> Unit = {},
        val buffer: ByteArray = ByteArray(3)) {
    init {
        controls.forEach { it.byteBuffer = buffer; it.sendMessage = sendMessage }
    }

    fun control(name: String): MidiHandler {
        return controls.first { it.name == name }
    }
}