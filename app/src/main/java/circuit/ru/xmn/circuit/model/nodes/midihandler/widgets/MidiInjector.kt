package circuit.ru.xmn.circuit.model.nodes.midihandler.widgets

import circuit.ru.xmn.circuit.model.nodes.midihandler.MidiHandlerData
import circuit.ru.xmn.circuit.model.nodes.midihandler.MidiHandlerNode
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.MidiControlType
import circuit.ru.xmn.circuit.model.presets.SynthMidiController
import java.util.*
import kotlin.collections.HashMap

data class MidiInjector(private val synths: List<SynthMidiController>, private val presetMidiSender: PresetMidiSender) {

    fun widgetNames() = LayoutWidget.widgetNames()
    fun layoutNode(widget: String) = LayoutWidget.node(widget, this)

    fun midiWidgetNames() = MidiWidget.widgetNames()
    fun midiHandlerNode(midiHandlerData: MidiHandlerData, widget: String) =
            register(
                    MidiHandlerNode(
                            midiHandlerData.midiHandler(),
                            MidiWidget.widget(widget),
                            presetMidiSender
                    )
            )

    fun controlNames(synthName: String) = synths.first { it.name == synthName }.controls.map { it.name }
    fun synthNames(): List<String> = synths.map { it.name }
    fun midiHandlerNode(synthName: String, midiControlName: String, widget: String) =
            register(
                    MidiHandlerNode(
                            synths.first { it.name == synthName }.control(midiControlName),
                            MidiWidget.widget(widget),
                            presetMidiSender
                    )
            )

    private fun register(midiHandler: MidiHandlerNode): MidiHandlerNode {
        presetMidiSender.register(midiHandler)
        return midiHandler
    }
}

class PresetMidiSender(
        private val sendToDevice: (ByteArray) -> Unit
) : SendMessage {

    private val byteBuffer: ByteArray = ByteArray(3)

    private val ccListeners = Array<Array<WeakHashMap<MidiHandlerNode, Unit>?>?>(16, { null })
    private val nrnpListeners = Array<HashMap<Int, WeakHashMap<MidiHandlerNode, Unit>>?>(16, { null })

    override fun send(midiControlType: MidiControlType, channel: Int, controlNumber: Int, value: Int) {
        midiControlType.sendMidiCommand(sendToDevice, byteBuffer, channel, controlNumber, value)

        when (midiControlType) {
            MidiControlType.CC -> {
                ccListeners[channel - 1]?.get(controlNumber - 1)
                        ?.forEachWeakMap { midiHandler, _ -> midiHandler.setValue(value) }
            }
            MidiControlType.NRPN -> {
                nrnpListeners[channel - 1]?.get(controlNumber - 1)
                        ?.forEachWeakMap { midiHandler, _ -> midiHandler.setValue(value) }
            }
        }
    }

    fun register(midiHandlerNode: MidiHandlerNode) {
        val channelNumber = midiHandlerNode.midiHandler.channel - 1
        val controlNumber = midiHandlerNode.midiHandler.controlNumber - 1
        when (midiHandlerNode.midiHandler.controlType) {
            MidiControlType.CC -> {
                if (ccListeners[channelNumber] == null)
                    ccListeners[channelNumber] = Array(128, { null })
                if (ccListeners[channelNumber]!![controlNumber] == null)
                    ccListeners[channelNumber]!![controlNumber] = WeakHashMap()

                ccListeners[channelNumber]!![controlNumber]!!.put(midiHandlerNode, Unit)
            }
            MidiControlType.NRPN -> {
                if (nrnpListeners[channelNumber] == null)
                    nrnpListeners[channelNumber] = HashMap()
                if (nrnpListeners[channelNumber]!![controlNumber] == null)
                    nrnpListeners[channelNumber]!![controlNumber] = WeakHashMap()

                nrnpListeners[channelNumber]!![controlNumber]!!.put(midiHandlerNode, Unit)
            }
        }
    }
}


interface SendMessage {
    fun send(midiControlType: MidiControlType, channel: Int, controlNumber: Int, value: Int)
}

inline fun <K, V> WeakHashMap<out K, V>.forEachWeakMap(action: (K, V) -> Unit) {
    for (element in this) action(element.key, element.value)
}