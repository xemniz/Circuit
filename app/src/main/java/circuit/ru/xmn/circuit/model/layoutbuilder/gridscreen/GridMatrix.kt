package circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen

import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder

class GridMatrix(val callback: Callback) {
    var matrixWrapper = MatrixWrapper()

    fun initMatrix(items: List<MidiGridItem>) {
        fillInSize(items, calculateMatrixSize(items))
        renderMatrix()
    }

    fun addGridItem(item: MidiGridItem) {
        when {
            !matrixWrapper.isInBounds(item) -> {
                val items = matrixWrapper.getItems().filter { it !is EmptyGridItem }

                fillInSize(items, calculateMatrixSize(items + item))
                matrixWrapper.setItem(item)
            }
            else -> matrixWrapper.setItem(item)
        }
        refillMatrix()
        renderMatrix()
    }

    fun remove(item: MidiGridItem) {
        matrixWrapper.removeItem(item)
        refillMatrix()
        renderMatrix()
    }

    private fun fillInSize(items: List<MidiGridItem>, size: Pair<Int, Int>) {
        val (rowsCount, columnsCount) = size
        matrixWrapper.refreshMatrixSize(rowsCount, columnsCount)
        items.forEach {
            matrixWrapper.setItem(it)
        }
        fillMatrixWithEmpties()
    }

    private fun refillMatrix() {
        fillInSize(matrixWrapper.getItems().filter { it !is EmptyGridItem }, calculateMatrixSize(matrixWrapper.getItems().filter { it !is EmptyGridItem }
        ))
    }

    private fun renderMatrix() {
        callback.clear()
        matrixWrapper.getItems().forEach { callback.add(it) }
    }

    private fun fillMatrixWithEmpties() {
        matrixWrapper.forEachIndexed { midiGridItem, row, column ->
            if (midiGridItem == null)
                matrixWrapper.setItem(callback.provideEmpty(row, column))
        }
    }

    private fun calculateMatrixSize(items: List<MidiGridItem>): Pair<Int, Int> {
        return items.fold(Pair(0, 0)) { acc, midiGridItem ->
            val rowsCount = Math.max(midiGridItem.gridPositionInfo.row + midiGridItem.gridPositionInfo.height - 1, acc.first)
            val columnsCount = Math.max(midiGridItem.gridPositionInfo.column + midiGridItem.gridPositionInfo.width - 1, acc.second)
            Pair(rowsCount, columnsCount)
        }
    }

    fun items(): List<MidiGridItem> = matrixWrapper.getItems().filter { it !is EmptyGridItem }

    interface Callback {
        fun clear()
        fun add(item: MidiGridItem)
        fun provideEmpty(row: Int, column: Int): MidiGridItem
    }
}

