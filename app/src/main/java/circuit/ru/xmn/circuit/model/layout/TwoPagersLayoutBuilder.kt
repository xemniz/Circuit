package circuit.ru.xmn.circuit.model.layout

import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.GridLayout
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.gridscreen.MidiGridScreenAdapter
import circuit.ru.xmn.circuit.model.presets.MidiControllerPreset
import kotlinx.android.synthetic.main.two_pagers_midi_controller.view.*
import ru.xmn.common.extensions.inflate

class TwoPagersLayoutBuilder(val preset: MidiControllerPreset, val sendMessage: (ByteArray) -> Unit) {

    fun addInto(container: FrameLayout) {
        val view = container.inflate(R.layout.two_pagers_midi_controller)
        container.addView(view)
        view.apply {
            firstPager.adapter = provideAdapter(preset, sendMessage)
            secondPager.adapter = provideAdapter(preset, sendMessage)
        }
    }

    private fun provideAdapter(preset: MidiControllerPreset, sendMessage: (ByteArray) -> Unit): PagerAdapter {
        return MidiControllerPagerAdapter(preset, sendMessage)
    }
}

class MidiControllerPagerAdapter(val preset: MidiControllerPreset, val sendMessage: (ByteArray) -> Unit) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val gridLayout = GridLayout(collection.context).apply { ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) }
        MidiGridScreenAdapter(preset.screens[position].gridControllers, sendMessage).bindInto(gridLayout)
        collection.addView(gridLayout)

        return gridLayout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return preset.screens.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

}