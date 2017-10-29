package circuit.ru.xmn.circuit.model.widgets

import android.view.ViewGroup
import it.beppi.knoblibrary.Knob

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