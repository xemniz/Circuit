package circuit.ru.xmn.circuit.model.common.addnodedialog

import android.app.Activity
import android.view.View
import android.view.ViewManager
import android.widget.AdapterView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.nodes.midihandler.MidiHandlerData
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.MidiControlType
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.Range
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import org.jetbrains.anko.*
import ru.xmn.common.extensions.views

internal fun <T> buildAddMidiDialog(activity: Activity, midiInjector: MidiInjector, dialogCustomParams: DialogCustomParams<T>, addItem: (T) -> Unit) {
    activity.alert {
        lateinit var widget: Spinner
        lateinit var midiControlProviderLayoutBuilder: MidiControlProviderLayoutBuilder
        customView {
            verticalLayout {
                padding = dip(16)
                widget = simpleSpinner("MidiWidget", midiInjector.midiWidgetNames())
                midiControlProviderLayoutBuilder = MidiControlProviderLayoutBuilder(midiInjector, widget)
                midiControlProviderLayoutBuilder.bind(this)
                dialogCustomParams.bind(this)
            }
        }
        positiveButton("add") {
            val builder = midiControlProviderLayoutBuilder.node()
            addItem(dialogCustomParams.get(builder))
        }
    }.show()
}

class MidiControlProviderLayoutBuilder(private val midiInjector: MidiInjector, private val widget: Spinner) {
    private lateinit var simpleSpinner: Spinner
    private lateinit var midiLayout: LinearLayout

    private lateinit var synthSpinner: Spinner

    private lateinit var name: EditText
    private lateinit var channel: EditText
    private lateinit var controlNumber: EditText
    private lateinit var controlType: EditText
    private lateinit var rangeBottom: EditText
    private lateinit var rangeTop: EditText
    private lateinit var offset: EditText
    private lateinit var defaultValue: EditText

    fun bind(viewManager: ViewManager) {
        viewManager.apply {
            viewManager.verticalLayout {
                simpleSpinner("Control from",
                        listOf("New control")
                                + midiInjector.synthNames()
                ) {
                    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                                    pos: Int, id: Long) {
                            when (pos) {
                                0 -> {
                                    bindNewMidiLayout()
                                }
                                else -> {
                                    bindSynthLayout()
                                }
                            }
                        }
                    }
                }

            }
            midiLayout = verticalLayout { }
        }
    }

    private fun bindNewMidiLayout() {
        midiLayout.views.forEach { midiLayout.removeView(it) }
        midiLayout.context.verticalLayout {
            name = labeledEditText("name")
            channel = labeledEditText("channel")
            controlNumber = labeledEditText("controlNumber")
            controlType = labeledEditText("controlType")
            rangeBottom = labeledEditText("rangeBottom")
            rangeTop = labeledEditText("rangeTop")
            offset = labeledEditText("offset")
            defaultValue = labeledEditText("defaultValue")
        }
    }

    private fun ViewManager.labeledEditText(label: String): EditText {
        lateinit var editText: EditText
        verticalLayout {
            textView {
                text = label
            }
            editText = editText()
        }
        return editText
    }

    private fun bindSynthLayout() {
        midiLayout.views.forEach { midiLayout.removeView(it) }
        midiLayout.context.verticalLayout {
            simpleSpinner("controls",
                    midiInjector.controlNames(simpleSpinner.selectedItem as String))
        }
    }

    fun node(): Node {
        return when (simpleSpinner.selectedItemPosition) {
            0 -> {
                midiInjector.midiHandlerNode(
                        MidiHandlerData(
                                name.text.toString(),
                                channel.text.toString().toInt(),
                                controlNumber.text.toString().toInt(),
                                MidiControlType.valueOf(controlType.text.toString()),
                                Range(rangeBottom.text.toString().toInt(), rangeTop.text.toString().toInt()),
                                offset.text.toString().toInt(),
                                defaultValue.text.toString().toInt()
                        ),
                        widget.selectedItem.toString())
            }
            else -> {
                midiInjector.midiHandlerNode(simpleSpinner.selectedItem as String, synthSpinner.selectedItem as String, widget.selectedItem.toString())
            }
        }
    }
}