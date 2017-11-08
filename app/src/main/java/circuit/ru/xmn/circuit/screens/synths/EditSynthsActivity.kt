package circuit.ru.xmn.circuit.screens.synths

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.presets.SynthMidiController
import circuit.ru.xmn.circuit.model.presets.SynthsRepository
import kotlinx.android.synthetic.main.activity_simple_list.*
import kotlinx.android.synthetic.main.item_simple.view.*
import ru.xmn.common.adapter.LastAdapter
import ru.xmn.common.adapter.bindItems
import ru.xmn.common.extensions.viewModelProvider

class EditSynthsActivity : AppCompatActivity() {
    private val synthsListViewModel: SynthsListViewModel by viewModelProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_list)
        setupViewModel()
    }

    private fun setupViewModel() {
        synthsListViewModel.synthsLiveData.observe(this, Observer { showList(it!!) })
    }

    private fun showList(list: List<SynthMidiController>) {
        synthsRecyclerView.bindItems(list.map { SynthItem(it) })
    }

    class SynthItem(val synthMidiController: SynthMidiController) : LastAdapter.Item() {
        override fun bindOn(view: View) {
            view.apply {
                text.text = synthMidiController.name
            }
        }

        override fun layoutId() = R.layout.item_simple

        override fun compare(anotherItemValue: Any): Boolean {
            return (anotherItemValue as? SynthMidiController)?.name?.equals(synthMidiController.name) ?: false
        }
    }
}

class SynthsListViewModel : ViewModel() {
    lateinit var synthsRepository: SynthsRepository
    val synthsLiveData: MutableLiveData<List<SynthMidiController>> = MutableLiveData()

    init {
        synthsRepository = SynthsRepository()
        synthsLiveData.value = synthsRepository.getSynths()
    }
}
