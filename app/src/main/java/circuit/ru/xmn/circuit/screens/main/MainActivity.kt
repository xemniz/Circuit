package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.media.midi.MidiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.grid.MidiScreenGridAdapter
import circuit.ru.xmn.circuit.screens.settings.MidiSettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {
    private val TAG = "MidiKeyboard"

    private lateinit var circuitViewModel: CircuitViewModel
    private var mMidiManager: MidiManager? = null
    private val mByteBuffer = ByteArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewmodel()
        setupListeners()
        setupToolbar()
    }

    private fun setupListeners() {
        settingsButton.setOnClickListener { startActivity<MidiSettingsActivity>() }
    }

    private fun setupViewmodel() {
        circuitViewModel = ViewModelProviders.of(this).get(CircuitViewModel::class.java)
        circuitViewModel.controllers.observe(this, Observer { controllers ->
            MidiScreenGridAdapter(controllerLayout, controllers!!) { midiSend(it) }
        })
    }


    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            title = "Main"
        }
    }

    private fun midiSend(buffer: ByteArray, count: Int = 3, timestamp: Long = System.nanoTime()) {
        circuitViewModel.midiSend(buffer, count, timestamp)
    }
}


