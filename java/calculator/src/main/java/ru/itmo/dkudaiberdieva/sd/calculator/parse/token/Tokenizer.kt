package ru.itmo.dkudaiberdieva.sd.calculator.parse.token

import ru.itmo.dkudaiberdieva.sd.calculator.parse.state.BraceState
import ru.itmo.dkudaiberdieva.sd.calculator.parse.state.NumberState
import ru.itmo.dkudaiberdieva.sd.calculator.parse.state.OperationState
import ru.itmo.dkudaiberdieva.sd.calculator.parse.state.State


class Tokenizer {

    private val tokens = ArrayList<Token>()

    private lateinit var state: State

    private val pointer = Pointer()

    fun tokenize(expression: String): List<Token> {
        while (pointer.p != expression.length) {
            val c = expression[pointer.p]
            when {
                OPERATIONS.contains(c) -> {
                    state = OperationState()
                    tokens.add(state.handle(expression, pointer))
                }
                BRACES.contains(c) -> {
                    state = BraceState()
                    tokens.add(state.handle(expression, pointer))
                }
                c.isDigit() -> {
                    state = NumberState()
                    tokens.add(state.handle(expression, pointer))
                }
                c.isWhitespace() -> {
                    pointer.p++
                }
                else -> throw IllegalStateException("Unexpected symbol ad $pointer")
            }
        }

        return tokens
    }

    companion object {
        val OPERATIONS = setOf('+', '-', '*', '/')
        val BRACES = setOf('(', ')')
    }
}

data class Pointer(var p: Int = 0) {
    override fun toString(): String {
        return p.toString()
    }
}