package circuit.ru.xmn.circuit

import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiManager
import android.media.midi.MidiReceiver
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val TAG = "MidiKeyboard"

    private var mKeyboardReceiverSelector: MidiInputPortSelector? = null
    private var mMidiManager: MidiManager? = null
    private val mByteBuffer = ByteArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            setupMidi()
        } else {
            Toast.makeText(this, "MIDI not supported!", Toast.LENGTH_LONG)
                    .show()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                midiCommand(MidiConstants.STATUS_CONTROL_CHANGE + 1, 80, p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun setupMidi() {
        mMidiManager = getSystemService(Context.MIDI_SERVICE) as MidiManager
        if (mMidiManager == null) {
            Toast.makeText(this, "MidiManager is null!", Toast.LENGTH_LONG)
                    .show()
            return
        }

        mKeyboardReceiverSelector = MidiInputPortSelector(mMidiManager!!,
                this, R.id.spinner_receivers)
    }


    private fun midiCommand(status: Int, data1: Int, data2: Int) {
        Log.d(TAG, "status: $status, data1: $data1, data2: $data2");
        mByteBuffer[0] = status.toByte()
        mByteBuffer[1] = data1.toByte()
        mByteBuffer[2] = data2.toByte()
        val now = System.nanoTime()
        midiSend(mByteBuffer, 3, now)
    }

    private fun midiSend(buffer: ByteArray, count: Int, timestamp: Long) {
        try {
            // send event immediately
            val receiver = getReceiver()
            receiver?.send(buffer, 0, count, timestamp)
        } catch (e: IOException) {
            Log.e(TAG, "mKeyboardReceiverSelector.send() failed " + e)
        }

    }

    private fun getReceiver(): MidiReceiver? {
        return mKeyboardReceiverSelector?.receiver
    }
}


