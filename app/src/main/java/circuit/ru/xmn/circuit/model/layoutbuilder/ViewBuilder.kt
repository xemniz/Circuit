package circuit.ru.xmn.circuit.model.layoutbuilder

import android.content.Context
import android.view.View

interface ViewBuilder {

    fun build(context: Context): View

}