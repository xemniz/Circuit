package ru.xmn.common.extensions

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup

fun <T> ViewPager.withAdapterFor(list: List<T>, buildChild: (ViewGroup, T) -> View){
    adapter = object : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val childView = buildChild(collection, list[position])
            collection.addView(childView)
            return childView
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int = list.count()

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view === `object`
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }
}