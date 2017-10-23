package circuit.ru.xmn.circuit

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.media.midi.MidiReceiver
import circuit.ru.xmn.circuit.model.grid.GridPositionInfo
import circuit.ru.xmn.circuit.model.grid.MidiGridController
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler
import ru.xmn.common.extensions.log
import java.io.IOException

class CircuitViewModel : ViewModel() {
    val controllers: MutableLiveData<List<MidiGridController>> = MutableLiveData()
    val synthDataProvider: SynthDataProvider = SynthDataProvider()

    init {
        val synthPreset = synthDataProvider.getSynthPreset(SynthDataProvider.NOVATION_CIRCUIT)
        controllers.value = synthPreset.controllers
    }

    fun midiSend(buffer: ByteArray, count: Int = 3, timestamp: Long = System.nanoTime()) {
        try {
            val receiver = getReceiver()
            receiver?.send(buffer, 0, count, timestamp)
        } catch (e: IOException) {
            log("mKeyboardReceiverSelector.send() failed " + e)
        }

    }

    private fun getReceiver(): MidiReceiver? {
        return null
    }
}

class SynthDataProvider {
    companion object {
        val NOVATION_CIRCUIT = "Novation circuit"
    }

    fun getSynthPreset(synthId: String): SynthGridPreset = SynthGridPreset()
}

class SynthGridPreset {
    val controllers: List<MidiGridController> = provideMacroFor1Synth(1,0) + provideMacroFor1Synth(2,2)

    private fun provideMacroFor1Synth(channel: Int, rowOffset: Int): List<MidiGridController> {
        return listOf(
                MidiGridController(
                        MidiHandler("Macro knob 1", channel-1, 80),
                        GridPositionInfo(1 + rowOffset, 1, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 2", channel-1, 81),
                        GridPositionInfo(1 + rowOffset, 2, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 3", channel-1, 82),
                        GridPositionInfo(1 + rowOffset, 3, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 4", channel-1, 83),
                        GridPositionInfo(1 + rowOffset, 4, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 5", channel-1, 84),
                        GridPositionInfo(2 + rowOffset, 1, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 6", channel-1, 85),
                        GridPositionInfo(2 + rowOffset, 2, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 7", channel-1, 86),
                        GridPositionInfo(2 + rowOffset, 3, 1)
                ),
                MidiGridController(
                        MidiHandler("Macro knob 8", channel-1, 87),
                        GridPositionInfo(2 + rowOffset, 4, 1)
                )
        )
    }
}

