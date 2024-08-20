package me.nooneboss

import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import me.nooneboss.data.auth.AuthSystem
import me.nooneboss.data.chat.ChatLogSystem
import me.nooneboss.data.logs.LogSystem
import me.nooneboss.data.questionnaire.QuestionnaireSystem
import me.nooneboss.plugins.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

object Systems {
    lateinit var authSystem: AuthSystem
    lateinit var questionnaireSystem : QuestionnaireSystem
    lateinit var chatLogSystem : ChatLogSystem
    lateinit var logSystem : LogSystem
}

fun Application.module() {
    val user = environment.config.propertyOrNull("databases.mongodb.user")?.getString() ?: "admin"
    val password = environment.config.propertyOrNull("databases.mongodb.password")?.getString() ?: "admin"
    val address = environment.config.propertyOrNull("databases.mongodb.address")?.getString() ?: "localhost"
    val port = environment.config.propertyOrNull("databases.mongodb.port")?.getString() ?: "27017"
    Systems.authSystem = AuthSystem(
        "mongodb://$user:$password@$address:$port",
        "gamedb"
    )
    Systems.questionnaireSystem = QuestionnaireSystem(
        "mongodb://$user:$password@$address:$port",
        "gamedb"
    )
    Systems.chatLogSystem = ChatLogSystem(
        "mongodb://$user:$password@$address:$port",
        "gamedb"
    )
    Systems.logSystem = LogSystem(
        "mongodb://$user:$password@$address:$port",
        "gamedb"
    )

    configureSerialization()
    configureSecurity()
    configureRouting()

    install(CORS) {
        anyHost()
    }
}