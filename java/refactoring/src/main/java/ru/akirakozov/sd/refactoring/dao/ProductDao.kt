package ru.akirakozov.sd.refactoring.dao

import ru.akirakozov.sd.refactoring.entity.Product
import java.sql.DriverManager
import java.sql.ResultSet

class ProductDao(private val dbUrl: String) {

    fun save(entity: Product) = DriverManager.getConnection(dbUrl).use { connection ->
        connection.prepareStatement(
            "INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"" + entity.name + "\"," + entity.price + ")"
        ).executeUpdate()
    }

    fun findAll(): List<Product> =
        executeQuery("SELECT * FROM PRODUCT", ENTITY_LIST_MAPPER)

    fun countAll(): Long =
        executeQuery("SELECT COUNT(*) FROM PRODUCT", LONG_VALUE_MAPPER)

    fun sum(): Long =
        executeQuery("SELECT SUM(price) FROM PRODUCT", LONG_VALUE_MAPPER)

    fun maxByPrice(): Product? =
        executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", ENTITY_LIST_MAPPER).firstOrNull()

    fun minByPrice(): Product? =
        executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1", ENTITY_LIST_MAPPER).firstOrNull()


    private fun <T> executeQuery(query: String, mapper: (rs: ResultSet) -> T): T =
        DriverManager.getConnection(dbUrl).use { connection ->
            mapper.invoke(connection.prepareStatement(query).executeQuery())
        }

    private companion object {

        val ENTITY_LIST_MAPPER: (ResultSet) -> List<Product> = { rs: ResultSet ->
            val result = ArrayList<Product>()
            while (rs.next()) {
                result.add(Product(rs.getLong(Product.ID), rs.getString(Product.NAME), rs.getInt(Product.PRICE)))
            }
            result
        }

        val LONG_VALUE_MAPPER: (ResultSet) -> Long = { rs ->
            if (rs.next()) rs.getLong(1) else 0L
        }
    }
}


