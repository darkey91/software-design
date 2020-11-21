package ru.akirakozov.sd.refactoring.enums

enum class QueryCommand(val command: String) {
    MAX("max"),
    MIN("min"),
    SUM("sum"),
    COUNT("count");
}