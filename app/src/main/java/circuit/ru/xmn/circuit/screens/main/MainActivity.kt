package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.screens.settings.MidiSettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    private lateinit var circuitViewModel: CircuitViewModel

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
        circuitViewModel.midiControllerPreset.observe(this, Observer {
            bindView(it!!)
        })
    }

    private fun bindView(controllerPreset: ViewBuilder) {
        val view = controllerPreset.build(baseContext).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        }
        midiControllerContainer.addView(view)
    }


    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            title = "Main"
        }
    }
}