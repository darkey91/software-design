package ru.itmo.dkudaiberdieva.sd.calculator.parse.token


class Tokenizer {

    fun tokenize(expression: String): List<Token> {
        TODO()
    }

    private fun handle(char: Char) {

    }

    companion object {
        val OPERATIONS = setOf('+', '-', '*', '/')
        val PARENS = setOf('(', ')')
    }
}