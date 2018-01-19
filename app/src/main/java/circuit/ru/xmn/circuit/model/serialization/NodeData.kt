package circuit.ru.xmn.circuit.model.serialization

import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector

interface NodeData{
    fun node(midiInjector: MidiInjector): Node
}