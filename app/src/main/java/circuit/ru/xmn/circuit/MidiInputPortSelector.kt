package circuit.ru.xmn.circuit

import android.app.Activity
import android.media.midi.*
import android.util.Log
import java.io.IOException

class MidiInputPortSelector
/**
 * @param midiManager
 * @param activity
 * @param spinnerId ID from the layout resource
 */
(midiManager: MidiManager, activity: Activity,
 spinnerId: Int) : MidiPortSelector(midiManager, activity, spinnerId, MidiDeviceInfo.PortInfo.TYPE_INPUT) {

    private var mInputPort: MidiInputPort? = null
    private var mOpenDevice: MidiDevice? = null

    override fun onPortSelected(wrapper: MidiPortWrapper?) {
        close()
        val info = wrapper?.deviceInfo
        if (info != null) {
            mMidiManager.openDevice(info, { device ->
                if (device == null) {
                    Log.e(MidiConstants.TAG, "could not open " + info)
                } else {
                    mOpenDevice = device
                    mInputPort = mOpenDevice!!.openInputPort(
                            wrapper.portIndex)
                    if (mInputPort == null) {
                        Log.e(MidiConstants.TAG, "could not open input port on " + info)
                    }
                }
            }, null)
            // Don't run the callback on the UI thread because openInputPort might take a while.
        }
    }

    val receiver: MidiReceiver?
        get() = mInputPort

    override fun onClose() {
        try {
            if (mInputPort != null) {
                Log.i(MidiConstants.TAG, "MidiInputPortSelector.onClose() - close port")
                mInputPort!!.close()
            }
            mInputPort = null
            if (mOpenDevice != null) {
                mOpenDevice!!.close()
            }
            mOpenDevice = null
        } catch (e: IOException) {
            Log.e(MidiConstants.TAG, "cleanup failed", e)
        }

        super.onClose()
    }
}