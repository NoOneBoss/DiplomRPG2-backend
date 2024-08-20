package me.nooneboss.data.chat

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

class ChatLogSystem(val connectionString: String, val databaseName: String) {
    private val mongoClient = MongoClients.create(connectionString)
    private val database: MongoDatabase = mongoClient.getDatabase(databaseName)
    private val collection: MongoCollection<Document> = database.getCollection("chat_logs")

    fun logMessage(chatLog: ChatLog) {
        val document = Document("user_uuid", chatLog.user_uuid)
            .append("timestamp", chatLog.timestamp)
            .append("message", chatLog.message)
            .append("chat_type", chatLog.chat_type)
        collection.insertOne(document)
    }

    fun logMessages(chatLogs: Array<ChatLog>) {
        val documents = chatLogs.map { chatLog ->
            Document("user_uuid", chatLog.user_uuid)
                .append("timestamp", chatLog.timestamp)
                .append("message", chatLog.message)
                .append("chat_type", chatLog.chat_type)
        }
        collection.insertMany(documents)
    }
}