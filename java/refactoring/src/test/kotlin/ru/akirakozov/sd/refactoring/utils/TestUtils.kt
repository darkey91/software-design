package ru.akirakozov.sd.refactoring.utils

import java.sql.DriverManager

fun prepareProductTable(dbUrl: String) {
    DriverManager.getConnection(dbUrl).use { connection ->
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
}