package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import circuit.ru.xmn.circuit.application.App
import circuit.ru.xmn.circuit.midiservice.MidiReceiverPortProvider
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.common.EditState
import circuit.ru.xmn.circuit.model.common.EditableState
import circuit.ru.xmn.circuit.model.common.NormalState
import circuit.ru.xmn.circuit.model.presets.CircuitSynthProvider
import circuit.ru.xmn.circuit.model.presets.PresetMidiController
import circuit.ru.xmn.circuit.model.presets.PresetsRepository
import javax.inject.Inject

class CircuitViewModel : ViewModel() {
    @Inject lateinit var outputPortProvider: MidiReceiverPortProvider
    @Inject lateinit var presetRepository: PresetsRepository
    val midiControllerPreset: MutableLiveData<PresetMidiController> = MutableLiveData()
    val editableState: MutableLiveData<EditableState> = MutableLiveData()

    init {
        App.component.inject(this)
        val circuitSynth = CircuitSynthProvider.provideCircuitSynth { midiSend(it) }
        val circuitPreset = CircuitPresetProvider.provide(MidiInjector(circuitSynth))
        midiControllerPreset.value = circuitPreset
        editableState.value = NormalState
    }

    fun midiSend(buffer: ByteArray) {
        outputPortProvider.midiSend(buffer)
    }

    fun loadPreset(presetName: String) {
        midiControllerPreset.value = presetRepository.getPreset(presetName)
    }

    fun changeState() {
        if (editableState.value!! is NormalState)
            editableState.value = EditState
        else
            editableState.value = NormalState
    }

    fun save(toString: String, currentPreset: PresetMidiController) {
        presetRepository.save(toString, currentPreset)
    }
}

