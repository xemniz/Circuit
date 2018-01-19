package circuit.ru.xmn.circuit.model.nodes.midihandler.widgets

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.MidiHandler

interface WidgetFactory {
    fun create(controlProvider: MidiInjector): Node
}

interface MidiWidgetFactory {
    fun create(context: Context, controller: MidiHandler, presetMidiSender: SendMessage): View
}