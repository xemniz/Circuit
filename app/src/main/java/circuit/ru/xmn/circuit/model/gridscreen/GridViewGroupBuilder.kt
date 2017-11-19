package circuit.ru.xmn.circuit.model.gridscreen

import android.content.Context
import android.support.v7.widget.GridLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import circuit.ru.xmn.circuit.model.layoutbuilder.DeletableViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.widgets.AddButtonWidget
import org.jetbrains.anko.*
import ru.xmn.common.extensions.getActivity
import ru.xmn.common.extensions.views

class GridViewGroupBuilder(private val initialChildes: List<MidiGridItem>, val midiControlProvider: MidiControlProvider) : ViewBuilder, GridMatrix.Callback {
    override fun provideEmpty(row: Int, column: Int): MidiGridItem {
        return emptyGridItem(gridLayout, row, column, {
            requestAddGridItem(row, column)
        })
    }

    override fun clear() {
        gridLayout.removeAllViews()

    }

    override fun add(item: MidiGridItem) {
        gridLayout.addView(bindParams(gridLayout, item))
    }

    private lateinit var matrix: GridMatrix
    private lateinit var gridLayout: GridLayout

    val childes: List<MidiGridItem>
        get() = matrix.items()

    override fun build(context: Context): View {
        gridLayout = GridLayout(context)
        matrix = GridMatrix(this)
        matrix.initMatrix(initialChildes)
        childes.map { bindParams(gridLayout, it) }.forEach { gridLayout.addView(it) }
        return gridLayout
    }

    fun bindParams(root: ViewGroup, builder: MidiGridItem): View {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(builder.gridPositionInfo.row - 1, 1f),
                GridLayout.spec(builder.gridPositionInfo.column - 1, 1f)
        ).apply {
            setGravity(Gravity.FILL)
            height = 0
            width = 0
        }

        val view = DeletableViewBuilder(builder, {
            requestRemoveGridItem(builder)
        }).build(root.context)
        view.setTag(builder)
        return view.apply { layoutParams = cellParams }
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
                        spinner {
                            adapter = ArrayAdapter<String>(
                                    context,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    midiControlProvider.controlNames())
                        }
                        textView {
                            text = "Widget"
                        }
                        spinner {
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

