package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler

object CircuitPresetsProvider {

    fun provideCircuitSynth(): SynthMidiController = SynthMidiController(listOf(
            MidiHandler("Synth 1 Macro 1", 1 - 1, 80),
            MidiHandler("Synth 1 Macro 2", 1 - 1, 81),
            MidiHandler("Synth 1 Macro 3", 1 - 1, 82),
            MidiHandler("Synth 1 Macro 4", 1 - 1, 83),
            MidiHandler("Synth 1 Macro 5", 1 - 1, 84),
            MidiHandler("Synth 1 Macro 6", 1 - 1, 85),
            MidiHandler("Synth 1 Macro 7", 1 - 1, 86),
            MidiHandler("Synth 1 Macro 8", 1 - 1, 87),
            MidiHandler("Synth 2 Macro 1", 2 - 1, 80),
            MidiHandler("Synth 2 Macro 2", 2 - 1, 81),
            MidiHandler("Synth 2 Macro 3", 2 - 1, 82),
            MidiHandler("Synth 2 Macro 4", 2 - 1, 83),
            MidiHandler("Synth 2 Macro 5", 2 - 1, 84),
            MidiHandler("Synth 2 Macro 6", 2 - 1, 85),
            MidiHandler("Synth 2 Macro 7", 2 - 1, 86),
            MidiHandler("Synth 2 Macro 8", 2 - 1, 87),
            MidiHandler("Synth1Level", 16 - 1, 12),
            MidiHandler("Synth2Level", 16 - 1, 14),
            MidiHandler("Filter res", 10 - 1, 71),
            MidiHandler("Filter freq", 10 - 1, 74),
            MidiHandler("Drum 1 level", 10 - 1, 12),
            MidiHandler("Drum 2 level", 10 - 1, 23),
            MidiHandler("Drum 3 level", 10 - 1, 45),
            MidiHandler("Drum 4 level", 10 - 1, 53)
    ))
}

class SynthMidiController(val controls: List<MidiHandler>, val sendMessage: (ByteArray) -> Unit)