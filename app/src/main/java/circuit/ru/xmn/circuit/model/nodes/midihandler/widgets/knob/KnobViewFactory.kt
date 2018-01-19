package circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.knob

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.MidiHandler
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiWidgetFactory
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.SendMessage
import ru.xmn.common.extensions.dpToPx

object KnobViewFactory : MidiWidgetFactory {
    override fun create(context: Context, controller: MidiHandler, presetMidiSender: SendMessage): View {
        val cell = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4.dpToPx, 4.dpToPx, 4.dpToPx, 4.dpToPx)

            addView(
                    KnobView(context).apply {
                        setKnobListener {
                            presetMidiSender.send(
                                    controller.controlType,
                                    controller.channel,
                                    controller.controlNumber,
                                    it.toInt()
                            )
                        }
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