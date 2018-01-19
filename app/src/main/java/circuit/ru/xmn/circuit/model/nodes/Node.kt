package circuit.ru.xmn.circuit.model.nodes

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.serialization.NodeData

interface DataOwner {
    fun data(): NodeData
}

interface ViewOwner{
    fun view(context: Context): View
}

interface Node: DataOwner, ViewOwner