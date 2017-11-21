package circuit.ru.xmn.circuit.model.gridscreen

import circuit.ru.xmn.circuit.model.widgets.MidiWidgetFactory
import circuit.ru.xmn.circuit.model.widgets.knob.KnobViewFactory

enum class Widget(val widgetName: String, val factory: MidiWidgetFactory) {
    KNOB("Knob", KnobViewFactory);

    companion object {
        fun widgetNames() = values().map { it.widgetName }
        fun widget(widgetName: String) = values().first { it.widgetName == widgetName }.factory
    }
}