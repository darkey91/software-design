package ru.akirakozov.sd.refactoring.servlet

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.DriverManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.test.assertEquals

class AddProductServletTest {

    val request = mock<HttpServletRequest>()
    val response = mock<HttpServletResponse>()

    val writer: StringWriter = StringWriter()
    val servlet = AddProductServlet()

    @BeforeEach
    fun setUp() {
        DriverManager.getConnection(DB_URL).use { connection ->
            connection
                .prepareStatement("DROP TABLE IF EXISTS PRODUCT")
                .execute()

            val sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)"
            connection.prepareStatement(sql)
                .execute()
        }
        given { response.writer }.willReturn(PrintWriter(writer))
    }

    @Test
    fun `add product`() {
        val (name, price) = Pair("expensive product", Integer.valueOf(100).toString())

        //given
        given { request.getParameter("name") }.willReturn(name)
        given { request.getParameter("price") }.willReturn(price)

        //when
        servlet.doGet(request, response)

        //then
        assertEquals(OK_RESPONSE, writer.toString())
    }

    @Test
    fun `add product with name equal to null`() {
        val (name, price) = Pair<String?, String?>(null, Integer.valueOf(100).toString())
        //given
        given { request.getParameter("name") }.willReturn(name)
        given { request.getParameter("price") }.willReturn(price)

        //when
        servlet.doGet(request, response)

        //then
        assertEquals(OK_RESPONSE, writer.toString())
    }

    @Test
    fun `add product with price equal to null`() {
        val (name, price) = Pair("expensive product", null)

        //given
        given { request.getParameter("name") }.willReturn(name)
        given { request.getParameter("price") }.willReturn(price)

        //when
        assertThrows<Exception> { servlet.doGet(request, response) }

        //then
        assertEquals("", writer.toString())
    }

    private companion object {
        //todo common part
        const val DB_URL = "jdbc:sqlite:test.db"
        const val OK_RESPONSE = "OK\n"
    }
}