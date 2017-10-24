package circuit.ru.xmn.circuit

import android.media.midi.MidiManager

class MidiService(val midiManager: MidiManager, val midiInputProvider: MidiPortProvider) {
}

class MidiPortProvider(val midiManager: MidiManager, val type: Int) {
    fun observePorts(onNewValue: (List<MidiPortWrapper>) -> Unit) {}
}
