package circuit.ru.xmn.circuit.model.nodes.midihandler

import android.content.Context
import android.view.View
import circuit.ru.xmn.circuit.model.nodes.Node
import circuit.ru.xmn.circuit.model.serialization.NodeData
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.MidiControlType
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.MidiHandler
import circuit.ru.xmn.circuit.model.nodes.midihandler.midicontrol.Range
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiWidget
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.MidiInjector
import circuit.ru.xmn.circuit.model.nodes.midihandler.widgets.SendMessage

data class MidiHandlerNode(val midiHandler: MidiHandler, val widgetFactory: MidiWidget, val presetMidiSender: SendMessage) : Node {
    override fun data(): NodeData {
        return MidiHandlerNodeData.from(this)
    }

    override fun view(context: Context): View {
        return widgetFactory.factory.create(context, midiHandler, presetMidiSender)
    }

    fun setValue(value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class MidiHandlerNodeData(val controller: MidiHandlerData, val widgetName: String) : NodeData {
    override fun node(midiInjector: MidiInjector) =
            midiInjector.midiHandlerNode(controller, widgetName)

    companion object {
        fun from(controller: MidiHandlerNode): NodeData {
            return MidiHandlerNodeData(MidiHandlerData.from(controller.midiHandler), controller.widgetFactory.widgetName)
        }
    }
}

class MidiHandlerData(
        val name: String,
        val channel: Int,
        val controlNumber: Int,
        val controlType: MidiControlType,
        val range: Range,
        val offset: Int,
        val defaultValue: Int) {
    fun midiHandler() = MidiHandler(name, channel, controlNumber, controlType, range, offset, defaultValue)

    companion object {
        fun from(handler: MidiHandler): MidiHandlerData {
            return MidiHandlerData(
                    handler.name,
                    handler.channel,
                    handler.controlNumber,
                    handler.controlType,
                    handler.range,
                    handler.offset,
                    handler.defaultValue
            )
        }
    }
}