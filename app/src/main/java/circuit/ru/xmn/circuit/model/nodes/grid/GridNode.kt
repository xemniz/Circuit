package circuit.ru.xmn.circuit.model.nodes.grid

import android.content.Context
import android.widget.GridLayout
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.common.EditableBottomButtonBuilder
import circuit.ru.xmn.circuit.model.common.withBottomButtons
import circuit.ru.xmn.circuit.model.common.CommonEditableParent
import circuit.ru.xmn.circuit.model.common.EditableParent
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.serialization.NodeData
import org.jetbrains.anko.gridLayout

data class GridNode(
        private val initialChildes: List<MidiGridItem>,
        private val midiInjector: MidiInjector)
    : Node, EditableParent by CommonEditableParent() {

    override fun data(): NodeData {
        return GridData.from(this)
    }

    lateinit var gridLayoutManager: GridLayoutManager

    val gridItems: List<MidiGridItem>
        get() = if (this::gridLayoutManager.isInitialized)
            gridLayoutManager.matrix.items()
        else
            initialChildes

    override fun view(context: Context) = withBottomButtons<GridLayout>(context) {
        initInner { gridLayout() }
        gridLayoutManager = GridLayoutManager(inner, initialChildes, midiInjector)
        addEditableChild(gridLayoutManager)
        button(EditableBottomButtonBuilder.ADD) {
            gridLayoutManager.requestAddGridItem(1, 1)
        }
    }


}

class MidiGridItemData(val position: GridPositionInfo, val item: NodeData)
