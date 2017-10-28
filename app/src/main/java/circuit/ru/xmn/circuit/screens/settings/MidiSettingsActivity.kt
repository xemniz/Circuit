package circuit.ru.xmn.circuit.screens.settings

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatSpinner
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.application.App
import circuit.ru.xmn.circuit.midiservice.MidiReceiverPortProvider
import circuit.ru.xmn.circuit.midiservice.MidiPortWrapper
import kotlinx.android.synthetic.main.activity_midi_settings.*
import javax.inject.Inject

class MidiSettingsActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<MidiPortWrapper>
    private lateinit var midiSettingsViewModel: MidiSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_midi_settings)

        setupSpinner()
        setupViewmodel()
        setupListeners()
        setupToolbar()
    }

    private fun setupSpinner() {
        adapter = ArrayAdapter<MidiPortWrapper>(baseContext, android.R.layout.simple_spinner_dropdown_item)
        val spinner = outputPortsSpinner as AppCompatSpinner
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        pos: Int, id: Long) {
                midiSettingsViewModel.selectPort(pos)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                midiSettingsViewModel.selectNothing()
            }
        }
    }

    private fun setupViewmodel() {
        midiSettingsViewModel = ViewModelProviders.of(this).get(MidiSettingsViewModel::class.java)
        midiSettingsViewModel.outpurPorts.observe(this, Observer { bindPorts(it!!) })
    }

    private fun setupListeners() {
        settingsOkButton.setOnClickListener { finish() }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            title = "Settings"
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun bindPorts(it: List<MidiPortWrapper>) {
        adapter.clear()
        adapter.addAll(it)
        outputPortsSpinner.setSelection(it.indexOfFirst { it.selected })
    }
}

class MidiSettingsViewModel : ViewModel() {
    @Inject
    lateinit var outputPortProvider: MidiReceiverPortProvider
    val outpurPorts: MutableLiveData<List<MidiPortWrapper>> = MutableLiveData<List<MidiPortWrapper>>()

    init {
        App.component.inject(this)
        outputPortProvider.observePorts { outpurPorts.value = it }
    }

    fun selectPort(i: Int) {
        outputPortProvider.selectPort(i)
    }

    fun selectNothing() {

    }
}
