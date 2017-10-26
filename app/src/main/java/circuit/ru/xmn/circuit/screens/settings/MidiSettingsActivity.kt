package circuit.ru.xmn.circuit.screens.settings

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import circuit.ru.xmn.circuit.R

class MidiSettingsActivity : AppCompatActivity() {
    private lateinit var midiSettingsViewModel: MidiSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_midi_settings)


        setupViewmodel()
    }


    private fun setupViewmodel() {
        midiSettingsViewModel = ViewModelProviders.of(this).get(MidiSettingsViewModel::class.java)
        midiSettingsViewModel
    }
}

class MidiSettingsViewModel: ViewModel() {

}
