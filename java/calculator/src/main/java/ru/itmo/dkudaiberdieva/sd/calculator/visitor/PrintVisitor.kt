package ru.itmo.dkudaiberdieva.sd.calculator.visitor

import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Brace
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.NumberToken
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Operation
import java.lang.StringBuilder


class PrintVisitor : TokenVisitor {
    private val stringBuilder = StringBuilder()

    override fun visit(token: NumberToken) {
        stringBuilder.append("${token.value} ")
    }

    override fun visit(token: Brace) {
        stringBuilder.append("${token.value} ")
    }

    override fun visit(token: Operation) {
        stringBuilder.append("${token.value} ")
    }

    fun getResult(): String {
        return stringBuilder.toString()
    }
}