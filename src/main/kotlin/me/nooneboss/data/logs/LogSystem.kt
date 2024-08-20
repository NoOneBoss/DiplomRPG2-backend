package me.nooneboss.data.logs

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

class LogSystem(val connectionString: String, val databaseName: String) {
    private val mongoClient = MongoClients.create(connectionString)
    private val database: MongoDatabase = mongoClient.getDatabase(databaseName)
    private val collection: MongoCollection<Document> = database.getCollection("logs")

    fun logMessage(log: ActionLog) {
        val document = Document("timestamp", log.timestamp)
            .append("uuid", log.uuid)
            .append("action", log.action)
            .append("context", log.context)
        collection.insertOne(document)
    }

    fun logMessages(logs: LogRequest) {
        val documents = logs.logs.map { log ->
            Document("timestamp", log.timestamp)
                .append("uuid", log.uuid)
                .append("action", log.action)
                .append("context", log.context)
        }
        collection.insertMany(documents)
    }

    fun fetchLogs(): List<ActionLog> {
        val logs = mutableListOf<ActionLog>()
        val documents = collection.find()
        for (document in documents) {
            val log = ActionLog(
                timestamp = document.getString("timestamp"),
                uuid = document.getString("uuid"),
                action = document.getString("action"),
                context = document.getString("context")
            )
            logs.add(log)
        }
        return logs
    }
}