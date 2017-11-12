package circuit.ru.xmn.circuit.model.gridscreen

import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder

class MidiGridItem(val gridPositionInfo: GridPositionInfo, val builder: ViewBuilder) : ViewBuilder by builder, Comparable<MidiGridItem> {
    override fun compareTo(other: MidiGridItem): Int =
            compareValuesBy(this, other, MidiGridItem::gridPositionInfo)
}

class GridPositionInfo(val row: Int,
                       val column: Int,
                       val width: Int = 1)
    : Comparable<GridPositionInfo> {
    override fun compareTo(other: GridPositionInfo): Int =
            compareValuesBy(this, other, GridPositionInfo::row, GridPositionInfo::column)
}