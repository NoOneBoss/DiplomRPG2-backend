package me.nooneboss.data.auth

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document
import java.util.*

class AuthSystem(val connectionString: String, val databaseName: String) {
    private val client: MongoClient = MongoClients.create(connectionString)
    private val database: MongoDatabase = client.getDatabase(databaseName)
    private val usersCollection: MongoCollection<Document> = database.getCollection("users")

    fun login(login: String, password: String): Boolean {
        val user = usersCollection.find(Document("login", login).append("password", password)).firstOrNull()
        return user != null
    }

    fun register(login: String, password: String): Boolean {
        val existingUser = usersCollection.find(Document("login", login)).firstOrNull()
        if (existingUser != null) {
            return false
        }

        val newUser = Document("login", login).append("password", password).append("uuid", UUID.randomUUID().toString())
        usersCollection.insertOne(newUser)
        return true
    }

    fun getUser(login: String): User? {
        val userDoc = usersCollection.find(Document("login", login)).firstOrNull()
        return userDoc?.let {
            User(
                uuid = UUID.fromString(it.getString("uuid")),
                login = it.getString("login"),
                password = it.getString("password")
            )
        }
    }
}
