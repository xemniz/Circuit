package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import circuit.ru.xmn.circuit.application.App
import circuit.ru.xmn.circuit.midiservice.MidiReceiverPortProvider
import circuit.ru.xmn.circuit.model.presets.CircuitSynthProvider
import circuit.ru.xmn.circuit.model.presets.CircuitPresetProvider
import javax.inject.Inject

class CircuitViewModel : ViewModel() {
    @Inject
    lateinit var outputPortProvider: MidiReceiverPortProvider
    val midiControllerPreset: MutableLiveData<MidiControllerPreset> = MutableLiveData()

    init {
        App.component.inject(this)

        val circuit = CircuitSynthProvider.provideCircuitSynth()
        val circuitFirstPreset = CircuitPresetProvider.provideFirst()
    }

    fun midiSend(buffer: ByteArray) {
        outputPortProvider.midiSend(buffer)
    }
}

