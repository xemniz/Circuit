package circuit.ru.xmn.circuit.model.common.addnodedialog

import android.app.Activity
import android.widget.Spinner
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import org.jetbrains.anko.*

internal fun <T> buildAddLayoutDialog(activity: Activity, midiInjector: MidiInjector, dialogCustomParams: DialogCustomParams<T>, addItem: (T) -> Unit) {
    activity.alert {
        lateinit var widget: Spinner

        customView {
            verticalLayout {
                padding = dip(16)
                widget = simpleSpinner("Layouts", midiInjector.widgetNames())
                dialogCustomParams.bind(this)
            }
        }
        positiveButton("add") {
            val item = dialogCustomParams.get(midiInjector.layoutNode(widget.selectedItem as String))
            addItem(item)
        }
    }.show()
}