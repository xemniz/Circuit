package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen.*
import circuit.ru.xmn.circuit.model.layoutbuilder.MidiControllerBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.PresetMidiController
import circuit.ru.xmn.circuit.model.widgets.MidiWidget
import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider

object CircuitPresetProvider {
    fun provide(controlProvider: MidiControlProvider) = PresetMidiController("Circuit Test Preset",
            autoFilledGrid(controlProvider,
                    listOf(
                            controlProvider.provide("drum 3 patch select", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("reverb drum 3 send level", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("delay drum 3 send level", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("drum 3 pitch", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("drum 3 decay", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("drum 3 distortion", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("drum 3 EQ", MidiWidget.KNOB.widgetName),
                            controlProvider.provide("drum 3 pan", MidiWidget.KNOB.widgetName)
                    ), 2
            ))

    fun autoFilledGrid(controlProvider: MidiControlProvider, controls: List<MidiControllerBuilder>, columnsCount: Int = 4): GridViewGroupBuilder {
        val midiGridControls = controls.mapIndexed { index, itemBuilder ->
            MidiGridItem(
                    GridPositionInfo((index / columnsCount) + 1, (index % columnsCount) + 1, 1),
                    itemBuilder
            )
        }
        return GridViewGroupBuilder(midiGridControls, controlProvider)
    }


}