package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresetsRepository @Inject constructor() {
    private val presets = listOf(
            CircuitPresetProvider.provide(MidiControlProvider(CircuitSynthProvider.provideCircuitSynth()))
    )

    fun getPresets() = presets

    fun getPreset(presetName: String) = presets.first() { it.name == presetName }
}