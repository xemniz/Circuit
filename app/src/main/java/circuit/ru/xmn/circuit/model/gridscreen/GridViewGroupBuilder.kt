package circuit.ru.xmn.circuit.model.gridscreen

import android.content.Context
import android.opengl.Matrix
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import circuit.ru.xmn.circuit.model.layoutbuilder.DeletableViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder

class GridViewGroupBuilder(childes: List<MidiGridItem>) : ViewBuilder {

    private val internalChildes: MutableList<MidiGridItem> = prepareInternalList(childes)

    private fun prepareInternalList(childes: List<MidiGridItem>): MutableList<MidiGridItem> {
        val internalList = childes.toMutableList()
        val (rowsCount, columnsCount) = internalList.fold(Pair(0, 0)) { acc, midiGridItem ->
            val rowsCount = Math.max(midiGridItem.gridPositionInfo.row, acc.first)
            val columnsCount = Math.max(midiGridItem.gridPositionInfo.column, acc.second)
            Pair(rowsCount, columnsCount)
        }
        return internalList
    }

    val childes: List<MidiGridItem>
        get() = internalChildes.toList()

    override fun build(context: Context): View {
        val gridLayout = GridLayout(context)
        internalChildes.map { bindParams(gridLayout, it) }.forEach { gridLayout.addView(it) }
        return gridLayout
    }

    fun bindParams(root: ViewGroup, builder: MidiGridItem): View {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(builder.gridPositionInfo.row - 1, 1f),
                GridLayout.spec(builder.gridPositionInfo.column - 1, 1f)
        ).apply {
            setGravity(Gravity.FILL)
            height = 0
            width = 0
        }

        val view = DeletableViewBuilder(builder, {
            removeChild(root, builder)
        }).build(root.context)
        view.setTag(builder)
        return view.apply { layoutParams = cellParams }
    }

    private fun removeChild(root: ViewGroup, builder: MidiGridItem) {
        root.removeView(root.findViewWithTag<View>(builder))
        internalChildes.remove(builder)
    }
}
