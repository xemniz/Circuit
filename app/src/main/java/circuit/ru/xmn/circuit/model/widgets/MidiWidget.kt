package circuit.ru.xmn.circuit.model.widgets

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen.GridViewGroupBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.pager.ViewPagerBuilder
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler
import circuit.ru.xmn.circuit.model.widgets.knob.KnobViewFactory

enum class MidiWidget(val widgetName: String, val factory: MidiWidgetFactory) {
    KNOB("Knob", KnobViewFactory);

    companion object {
        fun widgetNames() = values().map { it.widgetName }
        fun widget(widgetName: String) = values().first { it.widgetName == widgetName }.factory
    }
}

enum class Widget(val widgetName: String, val widgetFactory: WidgetFactory){
    PAGER("Pager", PagerViewFactory),
    GRID("Grid", GridViewFactory);

    companion object {
        fun widgetNames() = Widget.values().map { it.widgetName }
        fun widget(widgetName: String) = Widget.values().first { it.widgetName == widgetName }.widgetFactory
    }
}

object PagerViewFactory : WidgetFactory {
    override fun create(controlProvider: MidiControlProvider) =
        ViewPagerBuilder(listOf(), controlProvider)
}

object GridViewFactory : WidgetFactory {
    override fun create(controlProvider: MidiControlProvider) =
            GridViewGroupBuilder(listOf(), controlProvider)
}
