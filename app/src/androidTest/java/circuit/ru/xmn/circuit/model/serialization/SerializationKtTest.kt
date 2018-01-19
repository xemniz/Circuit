package circuit.ru.xmn.circuit.model.serialization

import com.google.gson.GsonBuilder
import org.junit.Test

class SerializationKtTest {
    @Test
    fun serialize() {
        val grid: RuntimeTypeAdapterFactory<Builder> = RuntimeTypeAdapterFactory.of(Builder::class.java)
                .registerSubtype(GridBuilder::class.java, "grid")
                .registerSubtype(PagerBuilder::class.java, "pager")
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(grid)
                .create()
        val toJson = gson.toJson(GridBuilder("one", listOf(PagerBuilder("pager"))) as Builder)
        println(toJson)
    }

    interface Builder

    class GridBuilder(private val name: String, private val items: List<Builder>): Builder
    class PagerBuilder(private val col: String): Builder

}