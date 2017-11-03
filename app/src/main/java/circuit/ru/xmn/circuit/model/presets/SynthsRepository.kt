package circuit.ru.xmn.circuit.model.presets

/**
 * Created by xmn on 02.11.2017.
 */
class SynthsRepository {
    fun getSynths() = listOf(
            CircuitSynthProvider.provideCircuitSynth()
    )
}