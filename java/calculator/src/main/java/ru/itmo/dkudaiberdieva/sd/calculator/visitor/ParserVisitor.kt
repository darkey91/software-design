package ru.itmo.dkudaiberdieva.sd.calculator.visitor

import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Brace
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.NumberToken
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Operation
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Token
import kotlin.collections.ArrayList

class ParserVisitor : TokenVisitor {
    private val stack = ArrayList<Token>()
    private val result = ArrayList<Token>()

    override fun visit(token: NumberToken) {
        result.add(token)
    }

    override fun visit(token: Brace) {
        if (token.value == '(') {
            stack.add(token)
            return
        }

        while (true) {
            if (stack.isEmpty())
                throw IllegalStateException("invalid parenthesis")

            val last = stack.removeLast()
            if (last is Brace)
                return

            result.add(last)
        }
    }

    override fun visit(token: Operation) {
        while (true) {
            val last = stack.lastOrNull()

            if (last !is Operation)
                break

            if (last.priority >= token.priority) {
                result.add(stack.removeLast())
            } else {
                break
            }
        }
        stack.add(token)
    }

    fun getResult(): List<Token> {
        while (stack.isNotEmpty()) {
            result.add(stack.removeLast())
        }
        return result
    }
}