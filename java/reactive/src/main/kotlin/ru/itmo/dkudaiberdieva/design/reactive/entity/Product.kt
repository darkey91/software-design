package ru.itmo.dkudaiberdieva.design.reactive.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency

@Document(collection = "product")
data class Product(
    @Id var id: Long? = null,
    var name: String? = null,
    var userId: Long? = null,   //userId - user's id who added this product
    var price: Long? = null,
    var currency: Currency? = null
)