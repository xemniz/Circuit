package circuit.ru.xmn.circuit.model.widgets

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler

interface WidgetFactory {
    fun create(controlProvider: MidiControlProvider): ViewBuilder
}

interface MidiWidgetFactory {
    fun create(context: Context, controller: MidiHandler): View
}