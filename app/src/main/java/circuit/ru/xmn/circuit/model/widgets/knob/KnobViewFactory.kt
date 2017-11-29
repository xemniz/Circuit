package circuit.ru.xmn.circuit.model.widgets.knob

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import circuit.ru.xmn.circuit.model.midicontrol.MidiHandler
import circuit.ru.xmn.circuit.model.widgets.MidiWidgetFactory
import ru.xmn.common.extensions.dpToPx

object KnobViewFactory : MidiWidgetFactory {
    override fun create(context: Context, controller: MidiHandler): View {
        val cell = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4.dpToPx, 4.dpToPx, 4.dpToPx, 4.dpToPx)

            addView(
                    KnobView(context).apply {
                        setKnobListener { controller.value = it.toInt() }
                        setMin(controller.range.lower.toDouble())
                        setMax(controller.range.upper.toDouble())
                        value = controller.currentValue.toDouble()
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