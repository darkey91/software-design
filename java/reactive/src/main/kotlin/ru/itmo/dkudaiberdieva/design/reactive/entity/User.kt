package ru.itmo.dkudaiberdieva.design.reactive.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.itmo.dkudaiberdieva.design.reactive.enums.Currency

@Document(collection = "user")
data class User(
    @Id var id: String? = null,
    var username: String? = null,
    var name: String? = null,
    var currency: Currency? = null
)
