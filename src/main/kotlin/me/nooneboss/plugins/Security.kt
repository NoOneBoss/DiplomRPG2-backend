package me.nooneboss.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nooneboss.Systems
import me.nooneboss.data.auth.User

private object JWTConfig{
    val realm = "Access to the /api path"

    fun generateToken(user: User, secret: String): String {
        return JWT.create()
            .withClaim("login", user.login)
            .withClaim("password", user.password)
            .sign(Algorithm.HMAC256(secret))
    }

    fun verifyToken(secret: String) : JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .build()
    }
}

fun Application.configureSecurity() {
    val secret = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "secret"

    authentication {
        jwt {
            realm = JWTConfig.realm
            verifier(JWTConfig.verifyToken(secret))
            validate{
                val userLogin = it.payload.getClaim("login").asString()
                val userPassword = it.payload.getClaim("password").asString()
                if(Systems.authSystem.login(userLogin, userPassword)){
                    Systems.authSystem.getUser(userLogin)
                }
                else null
            }
        }
    }

    routing {
        post("/login"){
            val formParameters = call.receiveParameters()
            val user = User(
                null,
                formParameters["login"].toString(),
                formParameters["password"].toString()
            )
            if(Systems.authSystem.login(user.login, user.password)){
                val token = JWTConfig.generateToken(user, secret)
                call.respondText(token)
                println("[LOG] User ${user.login} joined")
            }
            else {
                call.respondText("Failed!", status = HttpStatusCode.Unauthorized)
                println("[LOG] User ${user.login} failed to login!")
            }
        }

        post("/register"){
            val formParameters = call.receiveParameters()
            val user = User(
                null,
                formParameters["login"].toString(),
                formParameters["password"].toString()
            )
            if(Systems.authSystem.register(user.login, user.password)){
                println("[LOG] User ${user.login} registered")
                call.respondText ("Successfully!", status = HttpStatusCode.OK)
            }
            else call.respondText("Failed!", status = HttpStatusCode.Unauthorized)
        }
    }
}
