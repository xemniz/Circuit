package circuit.ru.xmn.circuit.model.serialization

import circuit.ru.xmn.circuit.model.nodes.grid.GridData
import circuit.ru.xmn.circuit.model.nodes.midihandler.MidiHandlerNodeData
import circuit.ru.xmn.circuit.model.nodes.pager.PagerData
import com.google.gson.GsonBuilder

val factory = RuntimeTypeAdapterFactory.of(NodeData::class.java)
        .registerSubtype(GridData::class.java, "grid")
        .registerSubtype(MidiHandlerNodeData::class.java, "controller")
        .registerSubtype(PagerData::class.java, "pager")

val gson = GsonBuilder()
        .registerTypeAdapterFactory(factory)
        .setPrettyPrinting()
        .create()
