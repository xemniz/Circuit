package circuit.ru.xmn.circuit.model.widgets

import circuit.ru.xmn.circuit.model.layoutbuilder.MidiControllerBuilder
import circuit.ru.xmn.circuit.model.presets.SynthMidiController

class MidiControlProvider(val synth: SynthMidiController) {
    fun provide(midiControl: String, widget: String) =
            MidiControllerBuilder(
                    synth.control(midiControl),
                    getWidgetFactory(widget)
            )

    fun layoutBuilder(widget: String) = Widget.widget(widget).create(this)


    private fun getWidgetFactory(widget: String): MidiWidgetFactory {
        return MidiWidget.widget(widget)
    }

    fun widgetNames() = Widget.widgetNames()
    fun midiWidgetNames() = MidiWidget.widgetNames()

    fun controlNames() = synth.controls.map { it.name }

    fun isMidiWidget(widget: String) = MidiWidget.values().any { it.widgetName == widget }

}