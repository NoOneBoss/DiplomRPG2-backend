package me.nooneboss

import io.ktor.server.application.*
import me.nooneboss.data.auth.AuthSystem
import me.nooneboss.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    AuthSystem

    configureSerialization()
    configureSecurity()
    configureRouting()
}
