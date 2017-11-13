package circuit.ru.xmn.circuit.model.widgets

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler
import it.beppi.knoblibrary.Knob
import ru.xmn.common.extensions.dpToPx

object KnobFactory : MidiWidgetFactory {
    override fun create(context: Context, controller: MidiHandler): View {
        val cell = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4.dpToPx, 4.dpToPx, 4.dpToPx, 4.dpToPx)

            addView(
                    Knob(context).apply {
                        setOnStateChanged { controller.value = it }
                        numberOfStates = controller.range.upper
                        isFreeRotation = false
                        minAngle = -150f
                        maxAngle = 150f
                        swipeSensibilityPixels = 10
                        swipeDirection = Knob.SWIPEDIRECTION_HORIZONTALVERTICAL
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f).apply {
                            setGravity(Gravity.CENTER_HORIZONTAL)
                        }
                    }
            )
            addView(
                    TextView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                            setGravity(Gravity.CENTER_HORIZONTAL)
                        }
                        text = controller.name
                        textSize = 10.toFloat()
                        setTextColor(Color.BLACK)
                    }
            )
        }

        return cell
    }
}