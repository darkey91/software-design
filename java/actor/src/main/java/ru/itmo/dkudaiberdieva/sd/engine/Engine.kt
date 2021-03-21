package ru.itmo.dkudaiberdieva.sd.engine;

val GOOGLE = Engine("Google", "localhost", 8095)
val YANDEX = Engine("Yandex", "localhost", 9000)
val BING = Engine("Bing", "localhost", 9005)
val ENGINES = listOf(GOOGLE, YANDEX, BING)

data class Engine(
    val name: String,
    val host: String,
    val port: Int
)
