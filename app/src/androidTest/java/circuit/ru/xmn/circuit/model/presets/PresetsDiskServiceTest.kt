package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class PresetsDiskServiceTest {
    val presetDiskService: PresetsDiskService = PresetsDiskService()
    val preset = CircuitPresetProvider.provide(MidiInjector(CircuitSynthProvider.provideCircuitSynth()))

    @Test
    fun presetNames() {
        presetDiskService.clear()
        presetDiskService.save("1", preset)
        presetDiskService.save("2", preset)
        assertEquals(listOf("1", "2"), presetDiskService.presetNames())
    }

    @Test
    fun preset() {
    }

    @Test
    fun save() {
        presetDiskService.clear()
        presetDiskService.save(preset.name, preset)
        val result = presetDiskService.preset(preset.name)
        Assert.assertEquals(preset.toString(), result.toString())
    }

}