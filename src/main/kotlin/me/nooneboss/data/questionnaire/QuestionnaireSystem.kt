package me.nooneboss.data.questionnaire

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import jetbrains.datalore.plot.config.getInt
import me.nooneboss.data.logs.ActionLog
import org.bson.Document

class QuestionnaireSystem(val connectionString: String, val databaseName: String) {
    private val mongoClient = MongoClients.create(connectionString)
    private val database: MongoDatabase = mongoClient.getDatabase(databaseName)
    private val collection: MongoCollection<Document> = database.getCollection("questionnaire")

    fun add(questionnaire: Questionnaire) {
        val document = Document("user_uuid", questionnaire.user_uuid)
            .append("s", questionnaire.s)
            .append("m", questionnaire.m)
            .append("t", questionnaire.t)
            .append("r", questionnaire.r)
        collection.insertOne(document)
    }

    fun fetch(): List<Questionnaire> {
        val questionnaires = mutableListOf<Questionnaire>()
        val documents = collection.find()
        for (document in documents) {
            val questionnaire = Questionnaire(
                user_uuid = document.getString("user_uuid"),
                s = document.getInt("s")!!,
                m = document.getInt("m")!!,
                t = document.getInt("t")!!,
                r = document.getInt("r")!!
            )
            questionnaires.add(questionnaire)
        }
        return questionnaires
    }
}