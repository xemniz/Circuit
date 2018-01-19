package circuit.ru.xmn.circuit.model.presets

import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.serialization.NodeData

data class PresetMidiController(val name: String, val node: Node)
class PresetMidiControllerData(val name: String, val node: NodeData)