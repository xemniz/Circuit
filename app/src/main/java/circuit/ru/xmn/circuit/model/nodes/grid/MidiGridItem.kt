package circuit.ru.xmn.circuit.model.nodes.grid

import circuit.ru.xmn.circuit.model.nodes.Node

open class MidiGridItem(val gridPositionInfo: GridPositionInfo, val node: Node) : Comparable<MidiGridItem> {
    override fun compareTo(other: MidiGridItem): Int =
            compareValuesBy(this, other, MidiGridItem::gridPositionInfo)

    override fun toString(): String {
        return "MidiGridItem(gridPositionInfo=$gridPositionInfo, node=$node)"
    }


}

class EmptyGridItem(gridPositionInfo: GridPositionInfo, builder: Node) : MidiGridItem(gridPositionInfo, builder)

data class GridPositionInfo(val row: Int,
                       val column: Int,
                       val width: Int = 1,
                       val height: Int = 1)
    : Comparable<GridPositionInfo> {
    override fun compareTo(other: GridPositionInfo): Int =
            compareValuesBy(this, other, GridPositionInfo::row, GridPositionInfo::column)
}