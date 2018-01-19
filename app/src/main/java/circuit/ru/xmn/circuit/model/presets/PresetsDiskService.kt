package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.application.App
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.serialization.gson
import java.io.File

class PresetsDiskService {
    private val folderPath = "${App.CONTEXT.filesDir.absolutePath}/presets/"
    private fun filePath(name: String) = "$folderPath$name.json"

    private fun serialize(currentPreset: Any) = gson.toJson(currentPreset)
    private fun deserialize(currentPreset: String) = gson.fromJson(currentPreset, PresetMidiControllerData::class.java)

    private fun checkFolder() {
        val folder = File(folderPath)
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    fun presetNames(): List<String> {
        return File(folderPath).listFiles().map { it.name.replace(".json", "") }
    }

    fun preset(name: String): PresetMidiController {
        checkFolder()

        val file = File(filePath(name))
        if (file.exists()) {
            val presetData: PresetMidiControllerData = deserialize(file.readText())
            return PresetMidiController(presetData.name, presetData.node.node(MidiInjector(CircuitSynthProvider.provideCircuitSynth())))
        } else
            throw IllegalArgumentException()
    }

    fun save(name: String, currentPreset: PresetMidiController) {
        checkFolder()

        val presetText: String = serialize(PresetMidiControllerData(name, currentPreset.node.data()))

        val file = File(filePath(name))
        if (!file.exists()) {
            file.createNewFile()
        }
        file.bufferedWriter().use { out ->
            out.write(presetText)
        }
    }

    fun clear() {
        File(folderPath).deleteRecursively()
    }
}