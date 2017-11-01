package circuit.ru.xmn.circuit.model.gridscreen

import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder

class MidiGridItem(val gridPositionInfo: GridPositionInfo, val builder: ViewBuilder) : ViewBuilder by builder

class GridPositionInfo(val row: Int,
                       val column: Int,
                       val width: Int = 1)