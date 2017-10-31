package circuit.ru.xmn.circuit.model.layoutbuilder

import android.content.Context
import android.view.View
import android.view.ViewGroup

interface LayoutBuilder<T : ViewBuilder> : ViewGroupBuilder<T> {

    override fun build(context: Context): View {
        val root = root(context)
        childes.map { bindParams(root, it) }.forEach { root.addView(it) }
        return root
    }

    fun bindParams(root: ViewGroup, builder: T): View

}