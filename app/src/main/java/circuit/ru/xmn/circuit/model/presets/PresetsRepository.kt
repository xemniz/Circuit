package circuit.ru.xmn.circuit.model.presets

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PresetsRepository @Inject constructor() {
    val presetsDiskService = PresetsDiskService()

    fun getPresets() = presetsDiskService.presetNames()

    fun getPreset(presetName: String) = presetsDiskService.preset(presetName)
    fun save(name: String, currentPreset: PresetMidiController) {
        presetsDiskService.save(name, currentPreset)
    }
}

