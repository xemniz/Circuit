package circuit.ru.xmn.circuit.model.layoutbuilder.pager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.layoutbuilder.*
import circuit.ru.xmn.circuit.model.layoutbuilder.adddialog.PagerDialogBinder
import circuit.ru.xmn.circuit.model.layoutbuilder.adddialog.createAddDialog
import circuit.ru.xmn.circuit.model.widgets.MidiControlProvider
import org.jetbrains.anko.image
import org.jetbrains.anko.imageButton
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.verticalLayout
import ru.xmn.common.extensions.getActivity
import ru.xmn.common.extensions.gone
import ru.xmn.common.extensions.visible


class ViewPagerBuilder(initialChildes: List<ViewBuilder>,
                       private val midiControlProvider: MidiControlProvider
) : ViewBuilder, EditableParent {

    lateinit var adapter: PagerAdapter

    val internalChildes: MutableList<DeletableViewBuilder> = initialChildes.map {
        DeletableViewBuilder(it, {
            remove(it)
        })
    }.toMutableList()

    override val editableChildes: MutableList<out Editable>
        get() = internalChildes

    private lateinit var addButton: ImageButton

    override fun parentChange(state: EditableState) = when (state) {
        is NormalState -> addButton.gone()
        is EditState -> addButton.visible()
    }

    private fun remove(viewBuilder: ViewBuilder) {
        val deletableViewBuilder = internalChildes.firstOrNull { it.editableChildes.any { it == viewBuilder } }
        internalChildes.remove(deletableViewBuilder)
        adapter.notifyDataSetChanged()
    }

    private fun add(viewBuilder: ViewBuilder) {
        val deletableViewBuilder = DeletableViewBuilder(viewBuilder, {
            remove(viewBuilder)
        })
        internalChildes += deletableViewBuilder
        adapter.notifyDataSetChanged()
    }

    override fun build(context: Context): View {
        adapter = object : PagerAdapter() {

            override fun instantiateItem(collection: ViewGroup, position: Int): Any {
                val childView = internalChildes[position].build(collection.context).apply { ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) }
                collection.addView(childView)
                return childView
            }

            override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
                collection.removeView(view as View)
            }

            override fun getCount(): Int = internalChildes.count()

            override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
                return view === `object`
            }

            override fun getItemPosition(`object`: Any): Int {
                return POSITION_NONE
            }
        }

        return context.verticalLayout {
            viewPager {
                adapter = this@ViewPagerBuilder.adapter
            }.lparams(width = matchParent, height = 0) {
                weight = 1f
            }
            addButton = imageButton {
                image = resources.getDrawable(R.drawable.ic_add_black_24dp)
                setOnClickListener { requestAddWidget() }
                gone()
            }.lparams {
                gravity = Gravity.END
            }
        }
    }

    private fun requestAddWidget() {
        addButton.getActivity()?.let {
            createAddDialog(it, midiControlProvider, PagerDialogBinder(), ::add)
        }
    }

}