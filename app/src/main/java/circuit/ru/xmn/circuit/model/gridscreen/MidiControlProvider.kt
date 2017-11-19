package circuit.ru.xmn.circuit.model.gridscreen

import circuit.ru.xmn.circuit.model.layoutbuilder.MidiControllerBuilder
import circuit.ru.xmn.circuit.model.presets.SynthMidiController
import circuit.ru.xmn.circuit.model.widgets.MidiWidgetFactory

class MidiControlProvider(val synth: SynthMidiController) {
    fun viewBuilder(midiControl: String, widget: String) =
            MidiControllerBuilder(
                    synth.control(midiControl),
                    getWidgetFactory(widget)
            )


    private fun getWidgetFactory(widget: String): MidiWidgetFactory {
        return Widget.widget(widget)
    }

    fun widgetNames() = Widget.widgetNames()

    fun controlNames() = synth.controls.map { it.name }

}