package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.gridscreen.GridPositionInfo
import circuit.ru.xmn.circuit.model.gridscreen.GridViewGroupBuilder
import circuit.ru.xmn.circuit.model.gridscreen.MidiGridItem
import circuit.ru.xmn.circuit.model.layoutbuilder.MidiControllerBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler
import circuit.ru.xmn.circuit.model.widgets.KnobFactory

object CircuitPresetProvider {
    fun provideFirst() = MidiControllerPreset(
            GridViewGroupBuilder(
                    listOf(
                            MidiGridItem(
                                    GridPositionInfo(1, 1),
                                    MidiControllerBuilder(
                                            MidiHandler(), KnobFactory
                                    )
                            ),
                            MidiGridItem(
                                    GridPositionInfo(1, 2),
                                    MidiControllerBuilder(
                                            MidiHandler(), KnobFactory
                                    )
                            ),
                            MidiGridItem(
                                    GridPositionInfo(2, 1),
                                    MidiControllerBuilder(
                                            MidiHandler(), KnobFactory
                                    )
                            ),
                            MidiGridItem(
                                    GridPositionInfo(2, 2),
                                    MidiControllerBuilder(
                                            MidiHandler(), KnobFactory
                                    )
                            )
                    )
            )
    )
}

class MidiControllerPreset(val builder: ViewBuilder)
