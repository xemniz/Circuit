package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import circuit.ru.xmn.circuit.application.App
import circuit.ru.xmn.circuit.midiservice.MidiReceiverPortProvider
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.presets.CircuitPresetProvider
import circuit.ru.xmn.circuit.model.presets.CircuitSynthProvider
import circuit.ru.xmn.circuit.model.presets.PresetsRepository
import javax.inject.Inject

class CircuitViewModel : ViewModel() {
    @Inject lateinit var outputPortProvider: MidiReceiverPortProvider
    @Inject lateinit var presetRepository: PresetsRepository
    val midiControllerPreset: MutableLiveData<ViewBuilder> = MutableLiveData()

    init {
        App.component.inject(this)
        val circuitSynth = CircuitSynthProvider.provideCircuitSynth { midiSend(it) }
        val circuitPreset = CircuitPresetProvider.provide(circuitSynth)
        midiControllerPreset.value = circuitPreset
    }

    fun midiSend(buffer: ByteArray) {
        outputPortProvider.midiSend(buffer)
    }

    fun loadPreset(presetName: String) {
        midiControllerPreset.value = presetRepository.getPreset(presetName)
    }
}

