package ru.itmo.dkudaiberdieva.sd.calculator

import ru.itmo.dkudaiberdieva.sd.calculator.parse.token.Tokenizer
import ru.itmo.dkudaiberdieva.sd.calculator.visitor.CalcVisitor
import ru.itmo.dkudaiberdieva.sd.calculator.visitor.ParserVisitor
import ru.itmo.dkudaiberdieva.sd.calculator.visitor.PrintVisitor

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("wrong usage")
    }

    val expr = args[0]
    var tokens = Tokenizer().tokenize(expr)

    //to reverse polish notation
    val parser = ParserVisitor()
    tokens.forEach {
        it.accept(parser)
    }
    tokens = parser.getResult()

    //print rpn
    val printer2 = PrintVisitor()
    tokens.forEach {
        it.accept(printer2)
    }
    println(printer2.getResult())

    //calculate
    val calc = CalcVisitor()
    tokens.forEach {
        it.accept(calc)
    }
    println(calc.getResult())
}