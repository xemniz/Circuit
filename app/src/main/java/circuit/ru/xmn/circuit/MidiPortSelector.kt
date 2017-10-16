package circuit.ru.xmn.circuit

import android.R
import android.app.Activity
import android.media.midi.MidiDeviceInfo
import android.media.midi.MidiDeviceStatus
import android.media.midi.MidiManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.util.*

abstract class MidiPortSelector
/**
 * @param midiManager
 * @param activity
 * @param spinnerId
 * ID from the layout resource
 * @param type
 * TYPE_INPUT or TYPE_OUTPUT
 */
(protected var mMidiManager: MidiManager, protected var mActivity: Activity,
 spinnerId: Int, type: Int) : MidiManager.DeviceCallback() {
    private var mType = MidiDeviceInfo.PortInfo.TYPE_INPUT
    protected var mAdapter: ArrayAdapter<MidiPortWrapper>
    protected var mBusyPorts = HashSet<MidiPortWrapper>()
    private val mSpinner: Spinner
    private var mCurrentWrapper: MidiPortWrapper? = null

    init {
        mType = type
        mSpinner = mActivity.findViewById<View>(spinnerId) as Spinner
        mAdapter = ArrayAdapter<MidiPortWrapper>(mSpinner.context,
                R.layout.simple_spinner_item)
        mAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
        mAdapter.add(MidiPortWrapper(null, 0, 0))

        mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                                        pos: Int, id: Long) {
                mCurrentWrapper = mAdapter.getItem(pos)
                onPortSelected(mCurrentWrapper!!)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                onPortSelected(null)
                mCurrentWrapper = null
            }
        }
        mSpinner.adapter = mAdapter

        MidiDeviceMonitor.getInstance(mMidiManager).registerDeviceCallback(this,
                Handler(Looper.getMainLooper()))

        val infos = mMidiManager.devices
        for (info in infos) {
            onDeviceAdded(info)
        }
    }

    /**
     * Set to no port selected.
     */
    fun clearSelection() {
        mSpinner.setSelection(0)
    }

    private fun getInfoPortCount(info: MidiDeviceInfo): Int {
        return if (mType == MidiDeviceInfo.PortInfo.TYPE_INPUT)
            info.inputPortCount
        else
            info.outputPortCount
    }

    override fun onDeviceAdded(info: MidiDeviceInfo) {
        val portCount = getInfoPortCount(info)
        for (i in 0..portCount - 1) {
            val wrapper = MidiPortWrapper(info, mType, i)
            mAdapter.add(wrapper)
            Log.i(MidiConstants.TAG, "$wrapper was added to " + this)
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onDeviceRemoved(info: MidiDeviceInfo) {
        val portCount = getInfoPortCount(info)
        for (i in 0..portCount - 1) {
            val wrapper = MidiPortWrapper(info, mType, i)
            val currentWrapper = mCurrentWrapper
            mAdapter.remove(wrapper)
            // If the currently selected port was removed then select no port.
            if (wrapper.equals(currentWrapper)) {
                clearSelection()
            }
            mAdapter.notifyDataSetChanged()
            Log.i(MidiConstants.TAG, "$wrapper was removed")
        }
    }

    override fun onDeviceStatusChanged(status: MidiDeviceStatus) {
        // If an input port becomes busy then remove it from the menu.
        // If it becomes free then add it back to the menu.
        if (mType == MidiDeviceInfo.PortInfo.TYPE_INPUT) {
            val info = status.deviceInfo
            Log.i(MidiConstants.TAG, "MidiPortSelector.onDeviceStatusChanged status = " + status
                    + ", mType = " + mType
                    + ", activity = " + mActivity.packageName
                    + ", info = " + info)
            // Look for transitions from free to busy.
            val portCount = info.inputPortCount
            for (i in 0..portCount - 1) {
                val wrapper = MidiPortWrapper(info, mType, i)
                if (!wrapper.equals(mCurrentWrapper)) {
                    if (status.isInputPortOpen(i)) { // busy?
                        if (!mBusyPorts.contains(wrapper)) {
                            // was free, now busy
                            mBusyPorts.add(wrapper)
                            mAdapter.remove(wrapper)
                            mAdapter.notifyDataSetChanged()
                        }
                    } else {
                        if (mBusyPorts.remove(wrapper)) {
                            // was busy, now free
                            mAdapter.add(wrapper)
                            mAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    /**
     * Implement this method to handle the user selecting a port on a device.
     *
     * @param wrapper
     */
    abstract fun onPortSelected(wrapper: MidiPortWrapper?)

    /**
     * Implement this method to clean up any open resources.
     */
    open fun onClose() {}

    /**
     * Implement this method to clean up any open resources.
     */
    fun onDestroy() {
        MidiDeviceMonitor.getInstance(mMidiManager).unregisterDeviceCallback(this)
    }

    /**
     *
     */
    fun close() {
        onClose()
    }
}

