package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.layoutbuilder.ViewBuilder
import circuit.ru.xmn.circuit.screens.presets.LoadPresetsActivity
import circuit.ru.xmn.circuit.screens.settings.MidiSettingsActivity
import circuit.ru.xmn.circuit.screens.synths.EditSynthsActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity
import ru.xmn.common.extensions.viewModelProvider

class MainActivity : AppCompatActivity() {

    private val circuitViewModel: CircuitViewModel by viewModelProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupListeners()
        setupToolbar()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.apply {
            val presetName = getStringExtra(PRESET_NAME)
            presetName?.let {
                circuitViewModel.loadPreset(presetName)
            }
        }
    }

    private fun setupListeners() {
        settingsButton.setOnClickListener { startActivity<MidiSettingsActivity>() }
        saveLoadButton.setOnClickListener {
            val editSynths = "Edit synths"
            val loadPreset = "Load preset"
            val saveCurrent = "Save current"
            val saveCurrentAs = "Save current as"
            val countries = listOf(editSynths, loadPreset, saveCurrent, saveCurrentAs)
            selector("Where are you from?", countries, { _, i ->
                when (countries[i]) {
                    editSynths -> {
                        startActivity<EditSynthsActivity>()
                    }
                    loadPreset -> {
                        startActivity<LoadPresetsActivity>()
                    }
                    saveCurrent -> {
                    }
                    saveCurrentAs -> {
                        
                    }
                }
            })
        }
    }

    private fun setupViewModel() {
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

    companion object  {
        const val PRESET_NAME = "PRESET_NAME"
    }
}

