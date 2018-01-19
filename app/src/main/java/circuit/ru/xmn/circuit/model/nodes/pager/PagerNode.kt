package circuit.ru.xmn.circuit.model.nodes.pager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import circuit.ru.xmn.circuit.model.common.*
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.common.addnodedialog.PagerDialogBinder
import circuit.ru.xmn.circuit.model.common.addnodedialog.createAddDialog
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.serialization.NodeData
import org.jetbrains.anko.support.v4.viewPager
import ru.xmn.common.extensions.getActivity
import ru.xmn.common.extensions.withAdapterFor


data class PagerNode(val initialChildes: List<Node>,
                private val midiInjector: MidiInjector
) : Node, EditableParent by CommonEditableParent() {

    override fun data() = PagerData.from(this)

    lateinit var adapter: PagerAdapter
    val internalChildes: MutableList<DeletableNode> = initialChildes.map {
        DeletableNode(it, {
            remove(it)
        })
    }.toMutableList()

    private fun remove(node: Node) {
        val deletableViewBuilder = internalChildes.firstOrNull { it.editableChildes.any { it == node } }
        removeChild(deletableViewBuilder)
        adapter.notifyDataSetChanged()
    }

    private fun removeChild(deletableViewBuilder: DeletableNode?) {
        internalChildes.remove(deletableViewBuilder)
        editableChildes.remove(deletableViewBuilder as Editable)
    }

    private fun add(node: Node) {
        val deletableViewBuilder = DeletableNode(node, {
            remove(node)
        })
        addChild(deletableViewBuilder)
        adapter.notifyDataSetChanged()
    }

    private fun addChild(deletableViewBuilder: DeletableNode) {
        internalChildes += deletableViewBuilder
        addEditableChild(deletableViewBuilder)
    }

    override fun view(context: Context) = withBottomButtons<ViewPager>(context) {
        initInner {
            viewPager {
                withAdapterFor(internalChildes) { container, item ->
                    item.view(container.context).apply { ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) }
                }
                this@PagerNode.adapter = adapter
            }
        }
        button(EditableBottomButtonBuilder.ADD) { requestAddWidget(inner) }
    }

    private fun requestAddWidget(view: View) {
        view.getActivity()?.let {
            createAddDialog(it, midiInjector, PagerDialogBinder(), ::add)
        }
    }

}

class PagerData(val items: List<NodeData>) : NodeData {
    override fun node(midiInjector: MidiInjector): Node {
        return PagerNode(items.map { it.node(midiInjector) }, midiInjector)
    }

    companion object {
        fun from(node: PagerNode): PagerData {
            return PagerData(node.internalChildes.map { it.node.data() })
        }
    }
}