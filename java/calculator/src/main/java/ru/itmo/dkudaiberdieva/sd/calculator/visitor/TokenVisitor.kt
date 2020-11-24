package ru.itmo.dkudaiberdieva.sd.calculator.visitor

import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Brace
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.NumberToken
import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Operation

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: Brace)
    fun visit(token: Operation)
}