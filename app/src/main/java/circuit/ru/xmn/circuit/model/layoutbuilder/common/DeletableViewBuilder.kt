package circuit.ru.xmn.circuit.model.layoutbuilder.common

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.editable.*
import ru.xmn.common.extensions.dpToPx
import ru.xmn.common.extensions.gone
import ru.xmn.common.extensions.inflate
import ru.xmn.common.extensions.visible

class DeletableViewBuilder(
        private val viewBuilder: ViewBuilder,
        private val deleteClicked: () -> Unit
) : ViewBuilder, EditableParent by CommonEditableParent() {
    override var currentState: EditableState = NormalState

    var deleteButton: View? = null

    init {
        (viewBuilder as? Editable)?.let { addEditableChild(it) }
        addEditableChild(object : CommonEditable() {
            override fun internalChange(state: EditableState) {
                when (state) {
                    is NormalState -> {
                        deleteButton?.gone()
                    }
                    is EditState -> {
                        deleteButton?.visible()
                    }
                }
            }
        })
    }

    override fun build(context: Context): View {
        return FrameLayout(context).apply {
            deleteButton = inflate(R.layout.round_button).apply {
                setParams()
                setOnClickListener { deleteClicked() }
                gone()
            }

            addView(viewBuilder.build(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT)
            })
            addView(deleteButton)
            deleteButton?.bringToFront()
        }
    }

    private fun View.setParams() {
        layoutParams = FrameLayout.LayoutParams(
                24.dpToPx,
                24.dpToPx).apply {
            gravity = Gravity.END or Gravity.TOP
            marginEnd = 4.dpToPx
            topMargin = 4.dpToPx
        }
        (this as ImageButton).setImageResource(R.drawable.ic_close_black_24dp)
    }
}