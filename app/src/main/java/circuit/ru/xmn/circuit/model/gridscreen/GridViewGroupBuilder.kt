package circuit.ru.xmn.circuit.model.gridscreen

import android.content.Context
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import circuit.ru.xmn.circuit.model.layoutbuilder.*

class GridViewGroupBuilder(val childes: List<MidiGridItem>) : ViewBuilder, EditCommandsResolver {
    override fun resolve(command: Command) {

    }

    override fun build(context: Context): View {
        val gridLayout = GridLayout(context)
        childes.map { bindParams(gridLayout, it) }.forEach { gridLayout.addView(it) }

        val root = FrameLayout(context).apply {
            addView(gridLayout.apply { layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT) })
        }
        return root
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

        return builder.build(root.context).apply { layoutParams = cellParams }
    }
}

interface EditCommandsResolver {
    fun resolve(command: Command)
}

sealed class Command
