package circuit.ru.xmn.circuit.model.widgets

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler

interface MidiWidgetFactory {
    fun create(context: Context, controller: MidiHandler): View
}