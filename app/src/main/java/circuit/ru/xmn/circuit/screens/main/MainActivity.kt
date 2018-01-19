package circuit.ru.xmn.circuit.screens.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.FrameLayout
import circuit.ru.xmn.circuit.R
import circuit.ru.xmn.circuit.model.common.Editable
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.screens.presets.LoadPresetsActivity
import circuit.ru.xmn.circuit.screens.settings.MidiSettingsActivity
import circuit.ru.xmn.circuit.screens.synths.EditSynthsActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import ru.xmn.common.extensions.viewModelProvider

class MainActivity : AppCompatActivity() {

    private val circuitViewModel: CircuitViewModel by viewModelProvider()
    private var currentPreset: Node? = null

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
        editButton.setOnClickListener { circuitViewModel.changeState() }
        settingsButton.setOnClickListener { startActivity<MidiSettingsActivity>() }
        saveLoadButton.setOnClickListener {
            val editSynths = "Edit synths"
            val loadPreset = "Load preset"
            val saveCurrent = "Save current"
            val saveCurrentAs = "Save current as"
            val countries = listOf(editSynths, loadPreset, saveCurrent, saveCurrentAs)
            selector("", countries, { _, i ->
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
                        alert {
                            lateinit var name: EditText
                            customView {
                                verticalLayout {
                                    name = editText()
                                }
                            }
                            positiveButton("save") {
                                currentPreset?.let { preset ->
                                    circuitViewModel.save(name.text.toString(), circuitViewModel.midiControllerPreset.value!!)
                                }
                            }
                        }.show()
                    }
                }
            })
        }
    }

    private fun setupViewModel() {
        circuitViewModel.midiControllerPreset.observe(this, Observer {
            bindView(it!!.node)
        })
        circuitViewModel.editableState.observe(this, Observer {
            (currentPreset as? Editable)?.change(it!!)
        })
    }

    private fun bindView(rootBuilder: Node) {
        currentPreset = rootBuilder.apply {
            val view = view(baseContext).apply {
                layoutParams = FrameLayout
                        .LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT)
            }
            midiControllerContainer.addView(view)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            title = "Main"
        }
    }

    companion object {
        const val PRESET_NAME = "PRESET_NAME"
    }
}

