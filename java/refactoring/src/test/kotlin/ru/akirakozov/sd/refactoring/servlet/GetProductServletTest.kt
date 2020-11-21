package ru.akirakozov.sd.refactoring.servlet

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.akirakozov.sd.refactoring.dao.ProductDao
import ru.akirakozov.sd.refactoring.utils.prepareProductTable
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.DriverManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.test.assertEquals

class GetProductServletTest {
    val request = mock<HttpServletRequest>()
    val response = mock<HttpServletResponse>()

    val writer: StringWriter = StringWriter()
    val productDao = ProductDao(DB_URL)
    val servlet = GetProductsServlet(productDao)

    @BeforeEach
    fun setUp() {
        prepareProductTable(DB_URL)
        given { response.writer }.willReturn(PrintWriter(writer))
    }

    @Test
    fun `get from empty table`() {
        //when
        servlet.doGet(request, response)

        //then
        assertEquals(getDOM(), writer.toString())
    }

    @Test
    fun `get all records`() {

        val (name1, price1) = "1" to 1
        val (name2, price2) = "2" to 2

        //given
        DriverManager.getConnection(DB_URL).use { connection ->
            connection.prepareStatement("INSERT INTO PRODUCT (NAME, PRICE) VALUES ('$name1', $price1), ('$name2', $price2)").execute()
        }

        //when
        servlet.doGet(request, response)

        //then
        assertEquals(getDOM("\n$name1\t$price1</br>\n$name2\t$price2</br>"), writer.toString())
    }

    private fun getDOM(result: String = "") = """<html><body>$result
        |</body></html>
        |""".trimMargin()


    private companion object {
        //todo common part
        const val DB_URL = "jdbc:sqlite:test.db"
    }
}