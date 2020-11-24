package ru.itmo.dkudaiberdieva.sd.calculator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Tokenizer
import ru.itmo.dkudaiberdieva.sd.calculator.visitor.CalcVisitor
import ru.itmo.dkudaiberdieva.sd.calculator.visitor.ParserVisitor
import kotlin.test.assertEquals

class MainTest {
    companion object {
        const val test1 = "3 + 4 * 2 / (5 - 1)"
        const val test2 = "(30 + 2) / 8"
        const val test3 = "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"
    }

    @Test
    fun testNoFailure() {
        assertEquals(5, getResult(test1))
        assertEquals(4, getResult(test2))
        assertEquals(1279, getResult(test3))
        assertEquals(6, getResult("2 + 2 * 2"))
        assertEquals(8, getResult("(2+ 2) * 2"))
        assertEquals(2, getResult("2  "))
        assertEquals(-1, getResult("( (1 - 2  ))"))
        assertEquals(6, getResult("2 *3"))
        assertEquals(6, getResult("2 * (3)"))
    }

    @Test
    fun `tests with failures`() {
        assertThrows<IllegalStateException> { getResult("2 * (3))") }
        assertThrows<IllegalStateException> { getResult("") }
        assertThrows<IllegalStateException> { getResult("()") }
        assertThrows<IllegalStateException> { getResult("1)") }
    }

    private fun getResult(expr: String): Int {
        var tokens = Tokenizer().tokenize(expr)

        //to reverse polish notation
        val parser = ParserVisitor()
        tokens.forEach {
            it.accept(parser)
        }
        tokens = parser.getResult()

        //calculate
        val calc = CalcVisitor()
        tokens.forEach {
            it.accept(calc)
        }
        return calc.getResult()
    }
}