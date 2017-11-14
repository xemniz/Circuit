package circuit.ru.xmn.circuit.model.gridscreen

import org.junit.Assert
import org.junit.Test
import org.junit.runners.JUnit4

/**
 * Created by USER on 14.11.2017.
 */
class GridViewGroupBuilderTest {
    @Test
    fun build() {
        Assert.assertEquals(2, test(2))
        Assert.assertEquals(3, test(3))
        Assert.assertEquals(5, test(4))
        Assert.assertEquals(8, test(5))
        Assert.assertEquals(13, test(6))
        Assert.assertEquals(21, test(7))
        Assert.assertEquals(34, test(8))
    }

    private fun test(n: Int): Int {
        var result = 0L
        var k = 0
        while (k <= n) {
            result += fact(n - k) / fact(n - 2 * k) / fact(k)
            k += 1
        }
        return result.toInt()
    }

    private fun fact(num: Int): Long {
        var factorial: Long = 1
        for (i in 1..num) {
            factorial *= i.toLong()
        }
        return factorial
    }

}