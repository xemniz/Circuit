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

    fun setItem(midiGridItem: MidiGridItem){
        println("${midiGridItem.gridPositionInfo}")
        matrixActionWithItem(midiGridItem) { item, row, col ->
            println("${item?.gridPositionInfo} $row $col")
            array[row][col] = item
        }
    }

    fun removeItem(midiGridItem: MidiGridItem) {
        matrixActionWithItem(midiGridItem) { item, row, col ->
            array[row][col] = null
        }
    }

    private fun matrixActionWithItem(item: MidiGridItem, action: (MidiGridItem?, Int, Int) -> Unit) {
        val rowInMatrix = item.gridPositionInfo.row - 1
        for (row in rowInMatrix until rowInMatrix + item.gridPositionInfo.height) {
            val columnInMatrix = item.gridPositionInfo.column - 1
            for (col in columnInMatrix until columnInMatrix + item.gridPositionInfo.width) {
                action(item, row, col)
            }
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
            .distinctBy { it.builder }
            .toList()
}