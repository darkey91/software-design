package ru.itmo.dkudaiberdieva.sd.calculator.visitor

import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Brace
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.NumberToken
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Operation
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Token

class CalcVisitor : TokenVisitor {
    val stack = ArrayList<Token>()

    override fun visit(token: NumberToken) {
        stack.add(token)
    }

    override fun visit(token: Brace) {
        //do nothing
    }

    override fun visit(token: Operation) {
        if (stack.size < 2) {
            throw IllegalStateException("Not enough operands for ${token.value}")
        }

        val rhs = stack.removeLast()
        val lhs = stack.removeLast()

        if (lhs !is NumberToken || rhs !is NumberToken) {
            throw IllegalStateException("Invalid format of expression: $token")
        }

        val res = when (token.value) {
            '+' -> NumberToken(lhs.value + rhs.value)
            '-' -> NumberToken(lhs.value - rhs.value)
            '*' -> NumberToken(lhs.value * rhs.value)
            '/' -> NumberToken(lhs.value / rhs.value)
            else -> throw IllegalStateException("Unexpected operator value: ${token.value}")
        }
        stack.add(res)
    }

    fun getResult(): Int {
        if (stack.size != 1)  {
            throw IllegalStateException("Invalid format of expression: stack size is ${stack.size} ")
        }

        if (stack.last() !is NumberToken) {
            throw IllegalStateException("Wrong type of the last token: ${stack.last()}")
        }

        val result = stack.removeLast() as NumberToken
        return result.value
    }
}