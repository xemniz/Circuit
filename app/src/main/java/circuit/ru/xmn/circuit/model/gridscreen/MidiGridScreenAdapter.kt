package circuit.ru.xmn.circuit.model.gridscreen

import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import circuit.ru.xmn.circuit.model.widgets.KnobFactory
import ru.xmn.common.extensions.dpToPx

class MidiGridScreenAdapter(val controllers: List<MidiGridController>,
                            val sendMessage: (ByteArray) -> Unit) {

    companion object {
        const val COLUMNS = 4
    }

    val byteBuffer: ByteArray = ByteArray(3)

    fun bindInto(container: GridLayout) {
        container.columnCount = COLUMNS
        controllers.forEach { controller ->
            val listener: (Int) -> Unit = { value -> sendMessage(controller.midiHandler.createMidiCommand(byteBuffer, value)) }
            bindView(container, controller, listener)
        }
    }

    private fun bindView(layout: GridLayout, controller: MidiGridController, listener: (Int) -> Unit) {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(controller.gridPositionInfo.row - 1, 1f),
                GridLayout.spec(controller.gridPositionInfo.column - 1, 1f)
        )

        val cell = LinearLayout(layout.context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = cellParams.apply {
                setGravity(Gravity.FILL)
                setPadding(4.dpToPx, 4.dpToPx, 4.dpToPx, 4.dpToPx)
                height = 0
                width = 0
            }
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

        layout.addView(cell)
    }
}

