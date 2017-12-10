package circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.common.DeletableViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.common.EditableBottomButtonBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.common.withBottomButtons
import circuit.ru.xmn.circuit.model.layoutbuilder.dialog.GridDialogBinder
import circuit.ru.xmn.circuit.model.layoutbuilder.dialog.createAddDialog
import circuit.ru.xmn.circuit.model.layoutbuilder.editable.CommonEditableParent
import circuit.ru.xmn.circuit.model.layoutbuilder.editable.Editable
import circuit.ru.xmn.circuit.model.layoutbuilder.editable.EditableParent
import circuit.ru.xmn.circuit.model.widgets.AddButtonWidget
import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider
import org.jetbrains.anko.gridLayout
import ru.xmn.common.extensions.getActivity

class GridViewGroupBuilder(
        private val initialChildes: List<MidiGridItem>,
        private val midiControlProvider: MidiControlProvider)
    : ViewBuilder, EditableParent by CommonEditableParent() {
    private lateinit var gridLayout: GridLayout

    lateinit var gridLayoutManager: GridLayoutManager
    val gridItems: List<MidiGridItem>
        get() = gridLayoutManager.matrix.items()

    override fun build(context: Context) = withBottomButtons<GridLayout>(context) {
        initInner { gridLayout() }
        gridLayoutManager = GridLayoutManager(inner, initialChildes, midiControlProvider)
        addEditableChild(gridLayoutManager)
        button(EditableBottomButtonBuilder.ADD) {
            gridLayoutManager.requestAddGridItem(1, 1)
        }
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

class GridLayoutManager(val gridLayout: GridLayout,
                        initialChildes: List<MidiGridItem>,
                        private val midiControlProvider: MidiControlProvider) :
        GridMatrix.Callback, EditableParent by CommonEditableParent() {

    private fun bindItemToLayout(root: ViewGroup, midiGridItem: MidiGridItem): ViewBuilder {
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

    fun requestAddGridItem(rowNumber: Int, columnNumber: Int) {
        gridLayout.getActivity()?.let {
            createAddDialog(it, midiControlProvider, GridDialogBinder(rowNumber, columnNumber), matrix::addGridItem)
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
                        object : ViewBuilder {
                            override fun build(context: Context): View {
                                return AddButtonWidget.create(root, onclick)
                            }
                        })
    }
}