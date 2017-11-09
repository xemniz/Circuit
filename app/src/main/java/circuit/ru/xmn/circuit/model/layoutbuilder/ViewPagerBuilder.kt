package circuit.ru.xmn.circuit.model.layoutbuilder

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup

class ViewPagerBuilder(override val childes: List<ViewBuilder>) {
    override fun root(context: Context): ViewPager {
        return ViewPager(context)
    }

    override fun build(context: Context): View {
        val root = root(context)
        root.adapter = object : PagerAdapter() {

            override fun instantiateItem(collection: ViewGroup, position: Int): Any {
                val childView = childes[0].build(collection.context).apply { ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) }
                collection.addView(childView)
                return childView
            }

            override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
                collection.removeView(view as View)
            }

            override fun getCount(): Int = childes.count()


            override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
                return view === `object`
            }
        }
        return root
    }

}