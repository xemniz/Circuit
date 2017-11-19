package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.gridscreen.*
import circuit.ru.xmn.circuit.model.layoutbuilder.MidiControllerBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.PresetMidiController

object CircuitPresetProvider {
    fun provide(controlProvider: MidiControlProvider) = PresetMidiController("Circuit Test Preset",
            autoFilledGrid(controlProvider,
                    listOf(
                            controlProvider.viewBuilder("drum 3 patch select", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("reverb drum 3 send level", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("delay drum 3 send level", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("drum 3 pitch", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("drum 3 decay", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("drum 3 distortion", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("drum 3 EQ", Widget.KNOB.widgetName),
                            controlProvider.viewBuilder("drum 3 pan", Widget.KNOB.widgetName)
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