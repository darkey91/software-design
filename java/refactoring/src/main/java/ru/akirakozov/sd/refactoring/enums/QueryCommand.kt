package ru.akirakozov.sd.refactoring.enums

enum class QueryCommand(val command: String) {
    MAX("max"),
    MIN("min"),
    SUM("sum"),
    COUNT("count"),
    UNKNOWN("unknown");

    companion object {
        private val allowedCommands = setOf(
            MAX.command, MIN.command,
            SUM.command, COUNT.command
        )

        @JvmStatic
        fun isAllowedCommand(command: String) = allowedCommands.contains(command)
    }
}