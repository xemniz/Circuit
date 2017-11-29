package circuit.ru.xmn.circuit.model.gridscreen

import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder

open class MidiGridItem(val gridPositionInfo: GridPositionInfo, val builder: ViewBuilder): Comparable<MidiGridItem> {
    override fun compareTo(other: MidiGridItem): Int =
            compareValuesBy(this, other, MidiGridItem::gridPositionInfo)
}

class EmptyGridItem(gridPositionInfo: GridPositionInfo, builder: ViewBuilder):MidiGridItem(gridPositionInfo, builder)

data class GridPositionInfo(val row: Int,
                       val column: Int,
                       val width: Int = 1,
                       val height: Int = 1)
    : Comparable<GridPositionInfo> {
    override fun compareTo(other: GridPositionInfo): Int =
            compareValuesBy(this, other, GridPositionInfo::row, GridPositionInfo::column)


}