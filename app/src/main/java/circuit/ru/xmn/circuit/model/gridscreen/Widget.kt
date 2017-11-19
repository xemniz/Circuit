package circuit.ru.xmn.circuit.model.gridscreen

import circuit.ru.xmn.circuit.model.widgets.KnobFactory
import circuit.ru.xmn.circuit.model.widgets.MidiWidgetFactory

enum class Widget(val widgetName: String, val factory: MidiWidgetFactory) {
    KNOB("Knob", KnobFactory);

    companion object {
        fun widgetNames() = values().map { it.name }
        fun widget(widgetName: String) = values().first { it.widgetName == widgetName }.factory
    }
}