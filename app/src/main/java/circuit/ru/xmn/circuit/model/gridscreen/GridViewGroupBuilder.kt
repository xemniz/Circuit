package circuit.ru.xmn.circuit.model.gridscreen

import android.content.Context
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import circuit.ru.xmn.circuit.model.layoutbuilder.*
import circuit.ru.xmn.circuit.model.widgets.AddButtonWidget
import org.jetbrains.anko.*
import ru.xmn.common.extensions.getActivity

class GridViewGroupBuilder(
        private val initialChildes: List<MidiGridItem>,
        private val midiControlProvider: MidiControlProvider)
    : ViewBuilder,
        GridMatrix.Callback,
        EditableParent {
    private lateinit var matrix: GridMatrix
    private lateinit var gridLayout: GridLayout

    val gridItems: List<MidiGridItem>
        get() = matrix.items()

    override fun build(context: Context): View {
        //todo wrap to frame layout with add and edit buttons
        gridLayout = GridLayout(context)
        matrix = GridMatrix(this)
        matrix.initMatrix(initialChildes)
        return gridLayout
    }

    fun bindItemToLayout(root: ViewGroup, midiGridItem: MidiGridItem): ViewBuilder {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(midiGridItem.gridPositionInfo.row - 1, midiGridItem.gridPositionInfo.height, midiGridItem.gridPositionInfo.height.toFloat()),
                GridLayout.spec(midiGridItem.gridPositionInfo.column - 1, midiGridItem.gridPositionInfo.width, midiGridItem.gridPositionInfo.width.toFloat())
        ).apply {
            setGravity(Gravity.FILL)
            height = 0
            width = 0
        }

        val deletableViewBuilder = DeletableViewBuilder(midiGridItem.builder, {
            requestRemoveGridItem(midiGridItem)
        })

        val view = deletableViewBuilder
                .build(root.context)
                .apply { layoutParams = cellParams }
        view.tag = midiGridItem
        gridLayout.addView(view)
        return deletableViewBuilder
    }

    private fun requestAddGridItem(rowNumber: Int, columnNumber: Int) {
        gridLayout.getActivity()?.apply {
            alert {
                var row: EditText? = null
                var column: EditText? = null
                var width: EditText? = null
                var height: EditText? = null
                var midiControl: Spinner? = null
                var widget: Spinner? = null
                title = "Create grid item"
                customView {
                    verticalLayout {
                        padding = dip(16)
                        textView {
                            text = "Midi control"
                        }
                        midiControl = spinner {
                            adapter = ArrayAdapter<String>(
                                    context,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    midiControlProvider.controlNames())
                        }
                        textView {
                            text = "Widget"
                        }
                        widget = spinner {
                            adapter = ArrayAdapter<String>(
                                    context,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    midiControlProvider.widgetNames())
                        }
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
                                width = editText {
                                    setText("1")
                                }
                            }
                            verticalLayout {
                                textView {
                                    text = "Height"
                                }
                                height = editText {
                                    setText("1")
                                }
                            }
                        }
                    }
                }
                positiveButton("Create") {
                    matrix.addGridItem(
                            row!!.text.toString(),
                            column!!.text.toString(),
                            width!!.text.toString(),
                            height!!.text.toString(),
                            midiControlProvider.viewBuilder(
                                    midiControl!!.selectedItem as String,
                                    widget!!.selectedItem as String)
                    )
                }
            }.show()
        }

    }

    private fun requestRemoveGridItem(builder: MidiGridItem) {
        matrix.remove(builder)
    }

    override fun provideEmpty(row: Int, column: Int): MidiGridItem {
        return emptyGridItem(gridLayout, row, column, {
            requestAddGridItem(row, column)
        })
    }

    override fun clear() {
        gridLayout.removeAllViews()
        editableChildes.clear()
    }

    override fun add(item: MidiGridItem) {
        val builder = bindItemToLayout(gridLayout, item)
        (builder as? Editable)?.let { editableChildes += it }
    }

    override val editableChildes: MutableList<Editable> = ArrayList()

    override fun parentChange(state: EditableState) {
        //todo show - hide FAB
    }

    companion object {
        fun emptyGridItem(root: ViewGroup, row: Int, column: Int, onclick: () -> Unit) =
                EmptyGridItem(GridPositionInfo(row, column, 1),
                        object : ViewBuilder {
                            override fun build(context: Context): View {
                                return AddButtonWidget.create(root, onclick)
                            }
                        })
    }
}

