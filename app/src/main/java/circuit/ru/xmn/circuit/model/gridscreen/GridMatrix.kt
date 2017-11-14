package circuit.ru.xmn.circuit.model.gridscreen

/**
 * Created by USER on 14.11.2017.
 */
class GridMatrix(rows: Int, columns: Int, val provideEmpty: () -> MidiGridItem) {
    val array = Array(rows) { arrayOfNulls<MidiGridItem>(columns) }

    fun fill(items: List<MidiGridItem>) {
        items.forEach {
            array[it.gridPositionInfo.row][it.gridPositionInfo.column] = it
        }

        array.forEachIndexed { row,  rowsList ->
            rowsList.forEachIndexed {column,  columnItem ->
                if (columnItem == null)
                    array[row][column] = provideEmpty()
            }
        }
    }
}