package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.gridscreen.GridPositionInfo
import circuit.ru.xmn.circuit.model.gridscreen.GridViewGroupBuilder
import circuit.ru.xmn.circuit.model.gridscreen.MidiGridItem
import circuit.ru.xmn.circuit.model.layoutbuilder.MidiControllerBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.PresetMidiController
import circuit.ru.xmn.circuit.model.widgets.KnobFactory

object CircuitPresetProvider {
    fun provide(circuitSynth: SynthMidiController) = PresetMidiController("Circuit Test Preset",
            autoFilledGrid(
                    listOf(
                            MidiControllerBuilder(circuitSynth.control("drum 3 patch select"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("reverb drum 3 send level"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("delay drum 3 send level"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("drum 3 pitch"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("drum 3 decay"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("drum 3 distortion"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("drum 3 EQ"), KnobFactory),
                            MidiControllerBuilder(circuitSynth.control("drum 3 pan"), KnobFactory)
                    ), 2
            ))

    fun autoFilledGrid(controls: List<MidiControllerBuilder>, columnsCount: Int = 4): GridViewGroupBuilder {
        val midiGridControls = controls.mapIndexed { index, itemBuilder ->
            MidiGridItem(
                    GridPositionInfo((index / columnsCount) + 1, (index % columnsCount) + 1, 1),
                    itemBuilder
            )
        }
        return GridViewGroupBuilder(midiGridControls)
    }


}