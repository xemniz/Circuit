package circuit.ru.xmn.circuit.model.layoutbuilder

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler
import circuit.ru.xmn.circuit.model.widgets.MidiWidgetFactory

class MidiControllerBuilder(val controller: MidiHandler, val widgetFactory: MidiWidgetFactory) : ViewBuilder {
    override fun build(context: Context): View {
        return widgetFactory.create(context, controller)
    }
}