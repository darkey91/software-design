package ru.akirakozov.sd.refactoring.servlet

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.DriverManager
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.test.assertEquals


class QueryServletTest {

    val request = mock<HttpServletRequest>()
    val response = mock<HttpServletResponse>()
    val writer: StringWriter = StringWriter()

    val queryServlet = QueryServlet()

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
    fun `max for no records - no result`() {
        //given
        given { request.getParameter("command") }.willReturn("max")

        //when
        queryServlet.doGet(request, response)

        //then
        assert(getDOM(MAX_QUERY_HEADER, "") == writer.toString())
    }

    @Test
    fun `max - max result`() {
        //given
        val (maxRecordName, maxRecordPrice) = Pair('3', 3)
        executeQuery("INSERT INTO PRODUCT (NAME, PRICE) VALUES ('1', 1), ('2', 2), ($maxRecordName, $maxRecordPrice)")
        given { request.getParameter("command") }.willReturn("max")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(MAX_QUERY_HEADER, "\n$maxRecordName\t$maxRecordPrice</br>"), writer.toString())
    }

    @Test
    fun `min for no records - no result`() {
        //given
        given { request.getParameter("command") }.willReturn("min")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(MIN_QUERY_HEADER, ""), writer.toString())
    }

    @Test
    fun `min - min result`() {
        //given
        val (minRecordName, minRecordPrice) = Pair('1', 1)
        executeQuery("INSERT INTO PRODUCT (NAME, PRICE) VALUES ($minRecordName, $minRecordPrice), ('2', 2), ('3', 3)")
        given { request.getParameter("command") }.willReturn("min")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(MIN_QUERY_HEADER, "\n$minRecordName\t$minRecordPrice</br>"), writer.toString())
    }

    @Test
    fun `sum for no records - 0`() {
        //given
        given { request.getParameter("command") }.willReturn("sum")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(SUM_QUERY_HEADER, "\n0"), writer.toString())
    }

    @Test
    fun `sum - sums all prices`() {
        //given
        executeQuery("INSERT INTO PRODUCT (NAME, PRICE) VALUES ('1', 1), ('2', 2), ('3', 3)")
        given { request.getParameter("command") }.willReturn("sum")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(SUM_QUERY_HEADER, "\n6"), writer.toString())
    }

    @Test
    fun `count no records - 0`() {
        //given
        given { request.getParameter("command") }.willReturn("count")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(COUNT_QUERY_HEADER, "\n0"), writer.toString())
    }

    @Test
    fun `count - counts all records`() {
        //given
        executeQuery("INSERT INTO PRODUCT (NAME, PRICE) VALUES ('1', 1), ('2', 2), ('3', 3)")
        given { request.getParameter("command") }.willReturn("count")

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals(getDOM(COUNT_QUERY_HEADER, "\n3"), writer.toString())
    }

    fun `unexpected command`() {
        val unexpectedCommand = "cound"
        given { request.getParameter("command") }.willReturn(unexpectedCommand)

        //when
        queryServlet.doGet(request, response)

        //then
        assertEquals("Unknown command: $unexpectedCommand", writer.toString())
    }

    private fun getDOM(header: String, result: String) = """<html><body>
        |$header$result
        |</body></html>
        |""".trimMargin()

    private fun executeQuery(sql: String) {
        DriverManager.getConnection(DB_URL).use { connection ->
            connection.prepareStatement(sql).execute()
        }
    }

    private companion object {
        //todo common part
        const val DB_URL = "jdbc:sqlite:test.db"

        const val MAX_QUERY_HEADER = "<h1>Product with max price: </h1>"
        const val MIN_QUERY_HEADER = "<h1>Product with min price: </h1>"
        const val SUM_QUERY_HEADER = "Summary price: "
        const val COUNT_QUERY_HEADER = "Number of products: "
    }
}