package circuit.ru.xmn.circuit.model.nodes.grid

import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.serialization.NodeData

class GridData(val items: List<MidiGridItemData>) : NodeData {
    override fun node(midiInjector: MidiInjector) =
            GridNode(
                    items.map { MidiGridItem(it.position, it.item.node(midiInjector)) },
                    midiInjector)

    companion object {
        fun from(grid: GridNode) = GridData(grid.gridItems
                .map { MidiGridItemData(it.gridPositionInfo, it.node.data()) })
    }
}