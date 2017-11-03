package circuit.ru.xmn.circuit.model.presets

/**
 * Created by xmn on 02.11.2017.
 */
class PresetsRepository {
    fun getPresets() = listOf(
            CircuitPresetProvider.provide(CircuitSynthProvider.provideCircuitSynth())
    )
}