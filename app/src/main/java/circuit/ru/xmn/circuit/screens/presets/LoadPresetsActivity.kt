package circuit.ru.xmn.circuit.screens.presets

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.presets.PresetMidiController
import circuit.ru.xmn.circuit.model.presets.PresetsRepository
import circuit.ru.xmn.circuit.screens.main.MainActivity
import kotlinx.android.synthetic.main.activity_simple_list.*
import kotlinx.android.synthetic.main.item_simple.view.*
import org.jetbrains.anko.intentFor
import ru.xmn.common.adapter.LastAdapter
import ru.xmn.common.adapter.bindItems
import ru.xmn.common.extensions.viewModelProvider

class LoadPresetsActivity : AppCompatActivity() {
    val presetsListViewModel: PresetsListViewModel by viewModelProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)
        setupViewModel()
    }

    private fun setupViewModel() {
        presetsListViewModel.presetsLiveData.observe(this, Observer { showList(it!!) })
    }

    private fun showList(list: List<PresetMidiController>) {
        synthsRecyclerView.bindItems(list.map { PresetItem(it) })
    }

    class PresetItem(val presetMidiController: PresetMidiController) : LastAdapter.Item() {
        override fun bindOn(view: View) {
            view.apply {
                text.text = presetMidiController.name
                setOnClickListener {
                    context.apply {
                        startActivity(intentFor<MainActivity>(MainActivity.PRESET_NAME to presetMidiController.name))
                    }
                }
            }
        }

        override fun layoutId() = R.layout.item_simple

        override fun compare(anotherItemValue: Any): Boolean {
            return (anotherItemValue as? PresetMidiController)?.name?.equals(presetMidiController.name) ?: false
        }
    }
}

class PresetsListViewModel : ViewModel() {
    lateinit var presetsRepository: PresetsRepository
    val presetsLiveData: MutableLiveData<List<PresetMidiController>> = MutableLiveData()

    init {
        presetsRepository = PresetsRepository()
        presetsLiveData.value = presetsRepository.getPresets()
    }
}