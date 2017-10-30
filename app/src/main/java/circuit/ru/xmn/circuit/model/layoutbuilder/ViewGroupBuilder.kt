package circuit.ru.xmn.circuit.model.layoutbuilder

import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import circuit.ru.xmn.circuit.model.widgets.KnobFactory
import ru.xmn.common.extensions.dpToPx

interface ViewBuilder<out SELF : MeasureData> {
    val measureData: SELF
    fun build(): View
}

interface ViewGroupBuilder<out SELF : MeasureData, CHILDES: MeasureData> : ViewBuilder<SELF> {
    val root: ViewGroup
    val childes: List<ViewBuilder<CHILDES>>
    fun bind(child: ViewBuilder<CHILDES>)

    override fun build(): View {
        childes.forEach { bind(it) }
        return root
    }
}

interface MeasureData

class GridMeasureData(val row: Int, val column: Int, val width: Int) : MeasureData

class GridViewGroupBuilder(
        override val measureData: MeasureData,
        override val childes: List<ViewBuilder<GridMeasureData>>,
        override val root: ViewGroup)
    : ViewGroupBuilder<MeasureData, GridMeasureData> {
    override fun bind(child: ViewBuilder<GridMeasureData>) {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(child.measureData.row - 1, 1f),
                GridLayout.spec(child.measureData.column - 1, 1f)
        ).apply {
            setGravity(Gravity.FILL)
            height = 0
            width = 0
        }

        val cell = child.build()
        cell.layoutParams = cellParams
        root.addView(cell)
    }
}

class KnobBuilder(override val measureData: GridMeasureData):ViewBuilder<GridMeasureData> {
    override fun build(): View {
        val cell = LinearLayout(layout.context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4.dpToPx, 4.dpToPx, 4.dpToPx, 4.dpToPx)

            addView(
                    KnobFactory.create(layout, listener).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1f).apply {
                            setGravity(Gravity.CENTER_HORIZONTAL)
                        }
                    }
            )
            addView(
                    TextView(layout.context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                            setGravity(Gravity.CENTER_HORIZONTAL)
                        }
                        text = controller.midiHandler.name
                        textSize = 10.toFloat()
                    }
            )
        }
        return cell
    }
}