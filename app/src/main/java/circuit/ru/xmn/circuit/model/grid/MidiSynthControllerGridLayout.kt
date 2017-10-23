package circuit.ru.xmn.circuit.model.grid

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TableLayout

class MidiSynthControllerGridLayout : TableLayout {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

    }

}
