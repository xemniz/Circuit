package circuit.ru.xmn.circuit.model.layoutbuilder.pager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.common.DeletableViewBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.common.EditableBottomButtonBuilder
import circuit.ru.xmn.circuit.model.layoutbuilder.common.withBottomButtons
import circuit.ru.xmn.circuit.model.layoutbuilder.dialog.PagerDialogBinder
import circuit.ru.xmn.circuit.model.layoutbuilder.dialog.createAddDialog
import circuit.ru.xmn.circuit.model.layoutbuilder.editable.*
import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider
import org.jetbrains.anko.support.v4.viewPager
import ru.xmn.common.extensions.getActivity
import ru.xmn.common.extensions.withAdapterFor


class ViewPagerBuilder(initialChildes: List<ViewBuilder>,
                       private val midiControlProvider: MidiControlProvider
) : ViewBuilder, EditableParent by CommonEditableParent() {

    lateinit var adapter: PagerAdapter
    val internalChildes: MutableList<DeletableViewBuilder> = initialChildes.map {
        DeletableViewBuilder(it, {
            remove(it)
        })
    }.toMutableList()

    private fun remove(viewBuilder: ViewBuilder) {
        val deletableViewBuilder = internalChildes.firstOrNull { it.editableChildes.any { it == viewBuilder } }
        removeChild(deletableViewBuilder)
        adapter.notifyDataSetChanged()
    }

    private fun removeChild(deletableViewBuilder: DeletableViewBuilder?) {
        internalChildes.remove(deletableViewBuilder)
        editableChildes.remove(deletableViewBuilder as Editable)
    }

    private fun add(viewBuilder: ViewBuilder) {
        val deletableViewBuilder = DeletableViewBuilder(viewBuilder, {
            remove(viewBuilder)
        })
        addChild(deletableViewBuilder)
        adapter.notifyDataSetChanged()
    }

    private fun addChild(deletableViewBuilder: DeletableViewBuilder) {
        internalChildes += deletableViewBuilder
        addEditableChild(deletableViewBuilder)
    }

    override fun build(context: Context) = withBottomButtons<ViewPager>(context) {
        initInner {
            viewPager {
                withAdapterFor(internalChildes){ container, item ->
                    item.build(container.context).apply { ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) }
                }
                this@ViewPagerBuilder.adapter = adapter
            }
        }
        button(EditableBottomButtonBuilder.ADD) { requestAddWidget(inner) }
    }

    private fun requestAddWidget(view: View) {
        view.getActivity()?.let {
            createAddDialog(it, midiControlProvider, PagerDialogBinder(), ::add)
        }
    }

}