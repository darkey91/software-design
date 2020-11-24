package ru.itmo.dkudaiberdieva.sd.calculator.parse.state

import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.*
import java.lang.StringBuilder

interface State {
    fun handle(expression: String, pointer: Pointer): Token
}

class BraceState: State {
    override fun handle(expression: String, pointer: Pointer): Token {
        val token = Brace(expression[pointer.p])
        pointer.p++
        return token
    }
}

class OperationState: State {
    override fun handle(expression: String, pointer: Pointer): Token {
        val token = Operation(expression[pointer.p])
        pointer.p++
        return token
    }
}

class NumberState: State {

    override fun handle(expression: String, pointer: Pointer): Token {
        val stringBuilder = StringBuilder()
        while (pointer.p < expression.length && expression[pointer.p].isDigit()) {
            stringBuilder.append(expression[pointer.p])
            pointer.p++
        }

        return NumberToken(Integer.valueOf(stringBuilder.toString()))
    }
}

