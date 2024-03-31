package me.nooneboss

import io.ktor.server.application.*
import me.nooneboss.data.auth.AuthSystem
import me.nooneboss.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

object Systems {
    lateinit var authSystem: AuthSystem
}

fun Application.module() {
    Systems.authSystem = AuthSystem(
            environment.config.propertyOrNull("databases.postgresql.address")?.getString() ?: "localhost",
            environment.config.propertyOrNull("databases.postgresql.port")?.getString() ?: "5432",
    )

    configureSerialization()
    configureSecurity()
    configureRouting()
}
