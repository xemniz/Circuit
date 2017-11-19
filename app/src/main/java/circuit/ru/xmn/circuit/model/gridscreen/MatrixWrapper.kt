package circuit.ru.xmn.circuit.model.gridscreen

/**
 * Care about indexes
 */
class MatrixWrapper(private var rows: Int = 1, private var columns: Int = 4) {
    var array = setMatrixSize(rows, columns)

    private fun setMatrixSize(rows: Int, columns: Int) = Array(rows) { arrayOfNulls<MidiGridItem>(columns) }

    fun refreshMatrixSize(rows: Int, columns: Int) {
        array = setMatrixSize(rows, columns)
    }

    fun setItem(midiGridItem: MidiGridItem): MutableList<MidiGridItem> {
        val itemsToRemove: MutableList<MidiGridItem> = ArrayList()
        matrixActionWithItem(midiGridItem) { item, row, col ->
            if (array[row][col] != null) {
                itemsToRemove += removeItem(array[row][col]!!)
            }
            array[row][col] = item
        }
        return itemsToRemove
    }

    fun removeItem(midiGridItem: MidiGridItem): MidiGridItem {
        val result = array[midiGridItem.gridPositionInfo.row - 1][midiGridItem.gridPositionInfo.column - 1]
        matrixActionWithItem(midiGridItem) { item, row, col ->
            array[row][col] = null
        }
        return result!!
    }

    private fun matrixActionWithItem(item: MidiGridItem, action: (MidiGridItem?, Int, Int) -> Unit) {
        for (row in 0..item.gridPositionInfo.height)
            for (col in 0..item.gridPositionInfo.width) {
                action(item, item.gridPositionInfo.row - 1 + row, item.gridPositionInfo.column - 1 + col)
            }
    }

    fun forEachIndexed(action: (MidiGridItem?, Int, Int) -> Unit) {
        array.forEachIndexed { row, rowsList ->
            rowsList.forEachIndexed { column, columnItem ->
                action(array[row][column], row + 1, column + 1)
            }
        }
    }

    fun isInBounds(item: MidiGridItem): Boolean {
        return rows >= item.gridPositionInfo.row - 1 + item.gridPositionInfo.height
                && columns >= item.gridPositionInfo.column - 1 + item.gridPositionInfo.width
    }

    fun getItems() = array.asSequence()
            .fold(ArrayList<MidiGridItem>()) { acc, arrayOfMidiGridItems ->
                val elements = arrayOfMidiGridItems.filter { it != null }.map { it!! }
                acc.addAll(elements)
                acc
            }
            .filter { it !is EmptyGridItem }
            .distinctBy { it.builder }
            .toList()
}