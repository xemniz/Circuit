package circuit.ru.xmn.circuit.model.widgets

import android.view.ViewGroup
import android.widget.ImageButton
import circuit.ru.xmn.circuit.R
import ru.xmn.common.extensions.inflate

object AddButtonWidget {
    fun create(root: ViewGroup, onclick: () -> (Unit)) =
            root.inflate(R.layout.round_button).apply {
                setOnClickListener { onclick() }
                (this as ImageButton).setImageResource(R.drawable.ic_add_black_24dp)
            }
}