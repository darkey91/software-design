package ru.itmo.dkudaiberdieva.sd.calculator.parse.token

import ru.itmo.dkudaiberdieva.sd.calculator.visitor.TokenVisitor

interface Token {
    fun accept(visitor: TokenVisitor)
}

class NumberToken(val value: String): Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

class Brace(val value: String): Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}

class Operation(val value: String): Token {
    override fun accept(visitor: TokenVisitor) {
        visitor.visit(this)
    }
}