package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.gridscreen.GridPositionInfo
import circuit.ru.xmn.circuit.model.gridscreen.MidiGridController
import circuit.ru.xmn.circuit.model.gridscreen.MidiGridScreen
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler

object CircuitPresetsProvider {
    fun provideTwoPanelsPreset() = MidiControllerPreset(listOf(
            macroScreen(1),
            macroScreen(2),
            levelScreen()
    ))

    fun fourRowsTwoColumnsScreen(controls: List<MidiHandler>, columnsCount: Int): MidiGridScreen {
        val midiGridControls = controls.mapIndexed { index, midiHandler ->
            MidiGridController(midiHandler, GridPositionInfo((index % columnsCount) + 1, (index / columnsCount) + 1, 1))
        }
        return MidiGridScreen(midiGridControls)
    }

    fun macroScreen(channel: Int): MidiGridScreen {
        return fourRowsTwoColumnsScreen(listOf(
                MidiHandler("Macro knob 1", channel - 1, 80),
                MidiHandler("Macro knob 2", channel - 1, 81),
                MidiHandler("Macro knob 3", channel - 1, 82),
                MidiHandler("Macro knob 4", channel - 1, 83),
                MidiHandler("Macro knob 5", channel - 1, 84),
                MidiHandler("Macro knob 6", channel - 1, 85),
                MidiHandler("Macro knob 7", channel - 1, 86),
                MidiHandler("Macro knob 8", channel - 1, 87)
        ), 4)
    }

    fun levelScreen(): MidiGridScreen {
        return fourRowsTwoColumnsScreen(listOf(
                MidiHandler("Synth1Level", 16 - 1, 12),
                MidiHandler("Synth2Level", 16 - 1, 14),
                MidiHandler("Filter res", 10 - 1, 71),
                MidiHandler("Filter freq", 10 - 1, 74),
                MidiHandler("Drum 1 level", 10 - 1, 12),
                MidiHandler("Drum 2 level", 10 - 1, 23),
                MidiHandler("Drum 3 level", 10 - 1, 45),
                MidiHandler("Drum 4 level", 10 - 1, 53)
        ), 4)
    }
}