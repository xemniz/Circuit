package circuit.ru.xmn.circuit.model.common.addnodedialog

import android.app.Activity
import android.content.DialogInterface
import android.view.ViewManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.nodes.grid.GridPositionInfo
import circuit.ru.xmn.circuit.model.nodes.grid.MidiGridItem
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import org.jetbrains.anko.*

fun <T> createAddDialog(
        activity: Activity,
        midiInjector: MidiInjector,
        dialogCustomParams: DialogCustomParams<T>,
        addItem: (T) -> Unit) {
    var dialog: DialogInterface? = null
    dialog = activity.alert {
        customView {
            verticalLayout {
                button("add midi node") {
                    setOnClickListener {
                        dialog?.dismiss()
                        buildAddMidiDialog(activity, midiInjector, dialogCustomParams, addItem)
                    }
                }
                button("add layout") {
                    setOnClickListener {
                        dialog?.dismiss()
                        buildAddLayoutDialog(activity, midiInjector, dialogCustomParams, addItem)
                    }
                }
            }
        }
    }.show()
}

internal fun ViewManager.simpleSpinner(label: String, controlNames: List<String>, initSpinner: Spinner.() -> Unit ={}): Spinner {
    textView {
        text = label
    }
    val spinner = spinner {
        adapter = ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                controlNames)
        initSpinner()
    }
    return spinner
}

interface DialogCustomParams<T> {
    fun bind(builder: ViewManager)
    fun get(builder: Node): T
}

class GridDialogBinder(private val rowNumber: Int,
                       private val columnNumber: Int) : DialogCustomParams<MidiGridItem> {
    lateinit var row: EditText
    lateinit var column: EditText
    lateinit var width: EditText
    lateinit var height: EditText

    override fun bind(builder: ViewManager) {
        builder.apply {
            linearLayout {
                verticalLayout {
                    textView {
                        text = "Row"
                    }
                    row = editText {
                        setText(rowNumber.toString())
                    }
                }
                verticalLayout {
                    textView {
                        text = "Column"
                    }
                    column = editText {
                        setText(columnNumber.toString())
                    }
                }
                verticalLayout {
                    textView {
                        text = "Width"
                    }
                    this@GridDialogBinder.width = editText {
                        setText("1")
                    }
                }
                verticalLayout {
                    textView {
                        text = "Height"
                    }
                    this@GridDialogBinder.height = editText {
                        setText("1")
                    }
                }
            }
        }
    }

    override fun get(builder: Node): MidiGridItem {
        return MidiGridItem(GridPositionInfo(
                row.text.toString().toInt(),
                column.text.toString().toInt(),
                width.text.toString().toInt(),
                height.text.toString().toInt()
        ), builder)
    }
}

class PagerDialogBinder : DialogCustomParams<Node> {
    override fun bind(builder: ViewManager) {
    }

    override fun get(builder: Node): Node {
        return builder
    }
}