package ru.itmo.dkudaiberdieva.design.reactive.enums

enum class Currency(private val inDollars: Double) {
    RUBLE(75.0), EURO(0.84), DOLLAR(1.0);

    fun convertToPreferredCurrency(price: Double, to: Currency): Double = price * this.inDollars * to.inDollars
}