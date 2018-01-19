package circuit.ru.xmn.circuit.model.common

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.ImageButton
import android.widget.LinearLayout
import circuit.ru.xmn.circuit.R
import org.jetbrains.anko.*
import ru.xmn.common.extensions.gone
import ru.xmn.common.extensions.visible

fun <T : View> EditableParent.withBottomButtons(context: Context, init: AddButtonWrapper<T>.() -> Unit): LinearLayout {
    val addButtonWrapper = AddButtonWrapper<T>(context)
    addButtonWrapper.init()
    addEditableChild(addButtonWrapper)
    return addButtonWrapper.layout
}

class AddButtonWrapper<T : View>(val context: Context): Editable {
    private var buttonsLayout: ViewGroup? = null
    private var internalInner: T? = null

    lateinit var layout: LinearLayout
    val inner
        get() = if (internalInner == null)
            throw IllegalStateException("First call init inner")
        else
            internalInner!!

    fun initInner(buildInner: ViewManager.() -> T) {
        layout = context.verticalLayout {
            internalInner = buildInner().lparams(width = matchParent, height = 0) {
                weight = 1f
            }
            buttonsLayout = linearLayout().lparams(width = matchParent, height = wrapContent) {
                gravity = Gravity.END
            }
        }
    }

    fun button(builder: EditableBottomButtonBuilder, click: () -> Unit) {
        if (buttonsLayout == null) throw IllegalStateException("First call init inner")
        val button = builder.build(context)
        button.setOnClickListener { click() }
        buttonsLayout!!.addView(button)
    }

    override var currentState: EditableState = NormalState

    override fun internalChange(state: EditableState) = when(state){
        NormalState -> {buttonsLayout?.gone(); Unit}
        EditState -> {buttonsLayout?.visible(); Unit}
    }
}

    enum class EditableBottomButtonBuilder {

        ADD {
            override fun build(context: Context): View {
                return ImageButton(context).apply {
                    setImageResource(R.drawable.ic_add_black_24dp)
                }
            }
        };

        abstract fun build(context: Context): View
}