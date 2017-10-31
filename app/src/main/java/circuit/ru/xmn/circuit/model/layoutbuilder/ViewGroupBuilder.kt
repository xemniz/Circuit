package circuit.ru.xmn.circuit.model.layoutbuilder

import android.content.Context
import android.view.ViewGroup

interface ViewGroupBuilder<out T : ViewBuilder> : ViewBuilder {
    fun root(context: Context): ViewGroup
    val childes: List<T>
}

