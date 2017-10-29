package circuit.ru.xmn.circuit.model.gridscreen

import android.support.v7.widget.GridLayout
import android.view.Gravity
import circuit.ru.xmn.circuit.model.widgets.KnobFactory
import ru.xmn.common.extensions.px

class MidiGridScreenAdapter(val layout: GridLayout,
                            controllers: List<MidiGridController>,
                            val sendMessage: (ByteArray) -> Unit) {

    companion object {
        const val COLUMNS = 4
    }

    val byteBuffer: ByteArray = ByteArray(3)

    init {
        layout.columnCount = COLUMNS
        controllers.forEach { controller ->
            val listener: (Int) -> Unit = { value -> sendMessage(controller.midiHandler.createMidiCommand(byteBuffer, value)) }
            bindView(controller.gridPositionInfo, listener)
        }
    }

    private fun bindView(gridPositionInfo: GridPositionInfo, listener: (Int) -> Unit) {
        val knob = KnobFactory.create(layout, listener)
        val params = GridLayout.LayoutParams(GridLayout.spec(gridPositionInfo.row - 1, 1f), GridLayout.spec(gridPositionInfo.column - 1, 1f))
        params.setGravity(Gravity.FILL)
        params.setMargins(4.px, 4.px, 4.px, 4.px)
        params.height = 0
        params.width = 0
        knob.layoutParams = params

        layout.addView(knob)
    }
}

