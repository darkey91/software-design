package ru.akirakozov.sd.refactoring.entity

class Product(
    val id: Long?,
    val name: String?,
    val price: Int?
) {
    constructor(name: String?, price: Int?): this(null, name, price)

    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val PRICE = "price"
    }
}
