package me.nooneboss.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nooneboss.data.User

fun Application.configureRouting() {
        routing {
            authenticate {
                get("/player") {
                    val user = call.principal<User>()
                    if (user == null) call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    else call.respond("Hello, ${user.login}!")
                }
            }
        }
}
