package ru.itmo.dkudaiberdieva.design.reactive.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency

@Document(collection = "product")
data class Product(
    @Id @Field("_id") var id: String? = null,
    var name: String? = null,
    var userId: String? = null,   //userId - user's id who added this product
    var price: Long? = null,
    var currency: Currency? = null
)