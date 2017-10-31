package circuit.ru.xmn.circuit.model.gridscreen

import android.content.Context
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import circuit.ru.xmn.circuit.model.layoutbuilder.LayoutBuilder

class GridViewGroupBuilder(
        override val childes: List<MidiGridItem>)
    : LayoutBuilder<MidiGridItem> {

    override fun root(context: Context) = GridLayout(context)

    override fun bindParams(root: ViewGroup, builder: MidiGridItem): View {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(builder.gridPositionInfo.row - 1, 1f),
                GridLayout.spec(builder.gridPositionInfo.column - 1, 1f)
        ).apply {
            setGravity(Gravity.FILL)
            height = 0
            width = 0
        }

        return builder.build(root.context).apply { layoutParams = cellParams }
    }
}