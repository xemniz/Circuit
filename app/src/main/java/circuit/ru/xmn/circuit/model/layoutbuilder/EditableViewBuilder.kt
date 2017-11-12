package circuit.ru.xmn.circuit.model.layoutbuilder

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import circuit.ru.xmn.circuit.R
import ru.xmn.common.extensions.dpToPx
import ru.xmn.common.extensions.gone
import ru.xmn.common.extensions.inflate
import ru.xmn.common.extensions.visible


class DeletableViewBuilder(val viewBuilder: ViewBuilder, val deleteClicked: () -> Unit) : ViewBuilder, Stateful {
    var deleteButton: View? = null

    override fun change(state: State) {
        when (state) {
            is NormalState -> {
                deleteButton?.visible()
            }
            is EditState -> {
                deleteButton?.gone()
            }
        }
    }

    override fun build(context: Context): View {
        val root = FrameLayout(context).apply {
            deleteButton = inflate(R.layout.round_button).apply {
                layoutParams = FrameLayout.LayoutParams(
                        24.dpToPx,
                        24.dpToPx).apply {
                    gravity = Gravity.END or Gravity.TOP
                    marginEnd = 4.dpToPx
                    topMargin = 4.dpToPx
                }
                (this as ImageButton).setImageResource(R.drawable.ic_close_black_24dp)
                setOnClickListener { deleteClicked() }
            }

            addView(viewBuilder.build(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT)
            })
            addView(deleteButton)
            deleteButton?.bringToFront()
        }

        return root
    }

}