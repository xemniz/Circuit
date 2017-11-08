package circuit.ru.xmn.circuit.model.presets

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PresetsRepository @Inject constructor() {
    private val presets = listOf(
            CircuitPresetProvider.provide(CircuitSynthProvider.provideCircuitSynth())
    )

    fun getPresets() = presets

    fun getPreset(presetName: String) = presets.first() { it.name == presetName }
}