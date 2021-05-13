package ru.itmo.sd.fitness.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


const val DB_NAME = "fitness"

@Configuration
@EnableMongoRepositories(basePackages = ["ru.itmo.sd.fitness.repository"])
class MongoConfig : AbstractMongoClientConfiguration() {

    @Override
    override fun getDatabaseName() = DB_NAME

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString("mongodb://localhost:27017/$DB_NAME")
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }
}