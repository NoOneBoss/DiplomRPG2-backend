package me.nooneboss.data.auth

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import java.util.*

data class User(val uuid: UUID?, val login: String, val password: String) : Principal {
}