package circuit.ru.xmn.circuit.model.nodes.grid

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.common.DeletableNode
import circuit.ru.xmn.circuit.model.common.addnodedialog.GridDialogBinder
import circuit.ru.xmn.circuit.model.common.addnodedialog.createAddDialog
import circuit.ru.xmn.circuit.model.common.CommonEditableParent
import circuit.ru.xmn.circuit.model.common.Editable
import circuit.ru.xmn.circuit.model.common.EditableParent
import circuit.ru.xmn.circuit.model.common.AddButtonWidget
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.serialization.NodeData
import ru.xmn.common.extensions.getActivity

class GridLayoutManager(val gridLayout: GridLayout,
                        initialChildes: List<MidiGridItem>,
                        private val midiInjector: MidiInjector) :
        GridMatrix.Callback, EditableParent by CommonEditableParent() {

    private fun bindItemToLayout(root: ViewGroup, midiGridItem: MidiGridItem): Editable {
        val cellParams = GridLayout.LayoutParams(
                GridLayout.spec(midiGridItem.gridPositionInfo.row - 1, midiGridItem.gridPositionInfo.height, midiGridItem.gridPositionInfo.height.toFloat()),
                GridLayout.spec(midiGridItem.gridPositionInfo.column - 1, midiGridItem.gridPositionInfo.width, midiGridItem.gridPositionInfo.width.toFloat())
        ).apply {
            setGravity(Gravity.FILL)
            height = 0
            width = 0
        }

        val deletableViewBuilder = DeletableNode(midiGridItem.node, {
            requestRemoveGridItem(midiGridItem)
        })

        val view = deletableViewBuilder
                .view(root.context)
                .apply { layoutParams = cellParams }
        view.tag = midiGridItem
        gridLayout.addView(view)
        return deletableViewBuilder
    }

    fun requestAddGridItem(rowNumber: Int, columnNumber: Int) {
        gridLayout.getActivity()?.let {
            createAddDialog(it, midiInjector, GridDialogBinder(rowNumber, columnNumber), matrix::addGridItem)
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
        (builder as? Editable)?.let {
            addEditableChild(it)
        }
    }

    val matrix = GridMatrix(this)

    init {
        matrix.initMatrix(initialChildes)
    }

    companion object {
        fun emptyGridItem(root: ViewGroup, row: Int, column: Int, onclick: () -> Unit) =
                EmptyGridItem(GridPositionInfo(row, column, 1),
                        object : Node {
                            override fun data(): NodeData {
                                //architecture issue
                                throw IllegalAccessException("it's not serializable")
                            }

                            override fun view(context: Context): View {
                                return AddButtonWidget.create(root, onclick)
                            }
                        })
    }
}