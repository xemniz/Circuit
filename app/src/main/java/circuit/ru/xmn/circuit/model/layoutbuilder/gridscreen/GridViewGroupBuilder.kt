package circuit.ru.xmn.circuit.model.layoutbuilder.gridscreen

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.layoutbuilder.*
import circuit.ru.xmn.circuit.model.layoutbuilder.adddialog.GridDialogBinder
import circuit.ru.xmn.circuit.model.layoutbuilder.adddialog.createAddDialog
import circuit.ru.xmn.circuit.model.widgets.AddButtonWidget
import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider
import org.jetbrains.anko.*
import ru.xmn.common.extensions.getActivity
import ru.xmn.common.extensions.gone
import ru.xmn.common.extensions.visible

class GridViewGroupBuilder(
        private val initialChildes: List<MidiGridItem>,
        private val midiControlProvider: MidiControlProvider)
    : ViewBuilder, EditableParent,
        GridMatrix.Callback {
    private lateinit var matrix: GridMatrix
    private lateinit var gridLayout: GridLayout

    val gridItems: List<MidiGridItem>
        get() = matrix.items()
    private lateinit var addButton: ImageButton

    override fun build(context: Context): View {
        val verticalLayout = context.verticalLayout {
            gridLayout = gridLayout().lparams(width = matchParent, height = 0) {
                weight = 1f
            }
            addButton = imageButton {
                image = resources.getDrawable(R.drawable.ic_add_black_24dp)
                setOnClickListener { requestAddGridItem(1, 1) }
                gone()
            }.lparams {
                gravity = Gravity.END
            }
        }
        matrix = GridMatrix(this)
        matrix.initMatrix(initialChildes)
        return verticalLayout
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
        (builder as? Editable)?.let { editableChildes += it }
    }

    override val editableChildes: MutableList<Editable> = ArrayList()

    override fun parentChange(state: EditableState) = when (state) {
        is NormalState -> addButton.gone()
        is EditState -> addButton.visible()
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

