package circuit.ru.xmn.circuit

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiManager
import android.media.midi.MidiReceiver
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import circuit.ru.xmn.circuit.model.grid.MidiScreenGridAdapter
import kotlinx.android.synthetic.main.activity_main.*
import ru.xmn.common.extensions.log
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val TAG = "MidiKeyboard"

    private lateinit var circuitViewModel: CircuitViewModel
    private var mKeyboardReceiverSelector: MidiInputPortSelector? = null
    private var mMidiManager: MidiManager? = null
    private val mByteBuffer = ByteArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewmodel()
        setupMidi()
    }

    private fun setupViewmodel() {
        circuitViewModel = ViewModelProviders.of(this).get(CircuitViewModel::class.java)
        circuitViewModel.controllers.observe(this, Observer { controllers ->
            MidiScreenGridAdapter(controllerLayout, controllers!!) { midiSend(it) }
        })
    }

    private fun setupMidi() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            mMidiManager = getSystemService(Context.MIDI_SERVICE) as MidiManager
            if (mMidiManager == null) {
                Toast.makeText(this, "MidiManager is null!", Toast.LENGTH_LONG)
                        .show()
                return
            }
            mKeyboardReceiverSelector = MidiInputPortSelector(mMidiManager!!, this, R.id.spinner_receivers)

        } else {
            Toast.makeText(this, "MIDI not supported!", Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun midiCommand(status: Int, data1: Int, data2: Int) {
        Log.d(TAG, "status: $status, data1: $data1, data2: $data2")
        mByteBuffer[0] = status.toByte()
        mByteBuffer[1] = data1.toByte()
        mByteBuffer[2] = data2.toByte()
        val now = System.nanoTime()
        midiSend(mByteBuffer, 3, now)
    }

    private fun midiSend(buffer: ByteArray, count: Int = 3, timestamp: Long = System.nanoTime()) {
        try {
            val receiver = getReceiver()
            receiver?.send(buffer, 0, count, timestamp)
        } catch (e: IOException) {
            log("mKeyboardReceiverSelector.send() failed " + e)
        }

    }

    private fun getReceiver(): MidiReceiver? {
        return mKeyboardReceiverSelector?.receiver
    }
}


