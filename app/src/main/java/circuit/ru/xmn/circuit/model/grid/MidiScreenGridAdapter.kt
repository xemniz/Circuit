package circuit.ru.xmn.circuit.model.grid

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TableLayout
import android.widget.TableRow
import com.sdsmdg.harjot.crollerTest.Croller
import it.beppi.knoblibrary.Knob
import ru.xmn.common.extensions.dp
import ru.xmn.common.extensions.px
import ru.xmn.common.extensions.views

class MidiScreenGridAdapter(val layout: GridLayout,
                            controllers: List<MidiGridController>,
                            val sendMessage: (ByteArray) -> Unit) {
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
        val params = GridLayout.LayoutParams(GridLayout.spec(gridPositionInfo.row - 1,1f), GridLayout.spec(gridPositionInfo.column - 1,1f))
        params.setGravity(Gravity.FILL)
        params.setMargins(4.px, 4.px, 4.px, 4.px)
        params.height = 64.px
        knob.layoutParams = params

        layout.addView(knob)
    }

    companion object {
        const val ROWS = 5
        const val COLUMNS = 4
    }
}

object KnobFactory {
    fun create(layout: ViewGroup, listener: (Int) -> Unit) = Knob(layout.context).apply {
        setOnStateChanged { listener(it) }
        numberOfStates = 127
        isFreeRotation = false
        minAngle = -150f
        maxAngle = 150f
        swipeSensibilityPixels = 10
        swipeDirection = Knob.SWIPEDIRECTION_HORIZONTALVERTICAL

    }


}
