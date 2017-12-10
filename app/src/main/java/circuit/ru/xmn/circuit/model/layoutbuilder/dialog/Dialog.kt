package circuit.ru.xmn.circuit.model.layoutbuilder.dialog

import android.app.Activity
import android.view.ViewManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen.GridPositionInfo
import circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen.MidiGridItem
import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider
import org.jetbrains.anko.*

fun <T> createAddDialog(
        activity: Activity,
        midiControlProvider: MidiControlProvider,
        dialogCustomParams: DialogCustomParams<T>,
        addItem: (T) -> Unit) {
    activity.alert {
        customView {
            verticalLayout {
                button("add midi widget") {
                    setOnClickListener {
                        buildAddMidiDialog(activity, midiControlProvider, dialogCustomParams, addItem)
                    }
                }
                button("add layout") {
                    setOnClickListener {
                        buildAddLayoutDialog(activity, midiControlProvider, dialogCustomParams, addItem)
                    }
                }
            }
        }
    }.show()
}

private fun <T> buildAddLayoutDialog(activity: Activity, midiControlProvider: MidiControlProvider, dialogCustomParams: DialogCustomParams<T>, addItem: (T) -> Unit) {
    activity.alert {
        lateinit var widget: Spinner

        customView {
            verticalLayout {
                padding = dip(16)
                widget = simpleSpinner("Layouts", midiControlProvider.widgetNames())
                dialogCustomParams.bind(this)
            }
        }
        positiveButton("add") {
            val item = dialogCustomParams.get(midiControlProvider.layoutBuilder(widget.selectedItem as String))
            addItem(item)
        }
    }.show()
}

private fun <T> buildAddMidiDialog(activity: Activity, midiControlProvider: MidiControlProvider, dialogCustomParams: DialogCustomParams<T>, addItem: (T) -> Unit) {
    activity.alert {
        lateinit var midiControl: Spinner
        lateinit var widget: Spinner
        customView {
            verticalLayout {
                padding = dip(16)
                midiControl = simpleSpinner("Midi control", midiControlProvider.controlNames())
                widget = simpleSpinner("MidiWidget", midiControlProvider.midiWidgetNames())
                dialogCustomParams.bind(this)
            }
        }
        positiveButton("add") {
            val builder = midiControlProvider
                    .provide(
                            midiControl.selectedItem as String,
                            widget.selectedItem as String
                    )
            addItem(dialogCustomParams.get(builder))
        }
    }.show()
}

private fun ViewManager.simpleSpinner(label: String, controlNames: List<String>): Spinner {
    textView {
        text = label
    }
    val spinner = spinner {
        adapter = ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                controlNames)
    }
    return spinner
}

interface DialogCustomParams<T> {
    fun bind(builder: ViewManager)
    fun get(builder: ViewBuilder): T
}

class GridDialogBinder(private val rowNumber: Int,
                       private val columnNumber: Int) : DialogCustomParams<MidiGridItem> {
    lateinit var row: EditText
    lateinit var column: EditText
    lateinit var width: EditText
    lateinit var height: EditText

    override fun bind(builder: ViewManager) {
        builder.apply {
            builder.linearLayout {
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

    override fun get(builder: ViewBuilder): MidiGridItem {
        return MidiGridItem(GridPositionInfo(
                row.text.toString().toInt(),
                column.text.toString().toInt(),
                width.text.toString().toInt(),
                height.text.toString().toInt()
        ), builder)
    }
}

class PagerDialogBinder : DialogCustomParams<ViewBuilder> {
    override fun bind(builder: ViewManager) {
    }

    override fun get(builder: ViewBuilder): ViewBuilder {
        return builder
    }
}