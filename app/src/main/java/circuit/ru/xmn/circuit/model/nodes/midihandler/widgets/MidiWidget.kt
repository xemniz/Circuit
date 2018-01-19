package circuit.ru.xmn.circuit.model.nodes.midihandler.widgets

import circuit.ru.xmn.circuit.model.nodes.grid.GridNode
import circuit.ru.xmn.circuit.model.nodes.pager.PagerNode
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.knob.KnobViewFactory

enum class MidiWidget(val widgetName: String, val factory: MidiWidgetFactory) {
    KNOB("Knob", KnobViewFactory);

    companion object {
        fun widgetNames() = values().map { it.widgetName }
        fun widget(widgetName: String) = values().first { it.widgetName == widgetName }
    }
}

enum class LayoutWidget(val widgetName: String, val widgetFactory: WidgetFactory){
    PAGER("Pager", PagerViewFactory),
    GRID("Grid", GridViewFactory);

    companion object {
        fun widgetNames() = LayoutWidget.values().map { it.widgetName }
        fun node(widgetName: String,
                 midiInjector: MidiInjector) =
                LayoutWidget.values().first { it.widgetName == widgetName }.widgetFactory.create(midiInjector)
    }
}

object PagerViewFactory : WidgetFactory {
    override fun create(controlProvider: MidiInjector) =
        PagerNode(listOf(), controlProvider)
}

object GridViewFactory : WidgetFactory {
    override fun create(controlProvider: MidiInjector) =
            GridNode(listOf(), controlProvider)
}
