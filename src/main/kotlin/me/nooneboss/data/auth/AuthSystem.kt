package me.nooneboss.data.auth

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.nooneboss.data.User
import org.ktorm.database.Database
import org.ktorm.database.asIterable

class AuthSystem(val ip: String, val port: String) {
    private fun createHikariDataSource(url: String, driver: String) = HikariDataSource(
        HikariConfig()
        .apply {
            driverClassName = driver
            jdbcUrl = url
            maximumPoolSize = 5
            username="postgres"
            password="postgres"
            validate()
        }
    )

    val database = Database.connect(createHikariDataSource(url = "jdbc:postgresql://$ip:$port/postgres", driver = "org.postgresql.Driver"))

    fun login(login: String, password: String) : Boolean{
        val login = database.useConnection { connection ->

            connection.prepareStatement("select login_user(?,?)").use { statement ->
                statement.setString(1, login)
                statement.setString(2, password)
                statement.executeQuery().asIterable().map { it.getBoolean(1) }
            }
        }

        return login.first()
    }

    fun register(login: String, password: String) : Boolean{
        val statement = database.useConnection { connection ->
            connection.prepareStatement("select register_user(?,?)").use { statement ->
                statement.setString(1, login)
                statement.setString(2, password)
                statement.executeQuery().asIterable().map { it.getBoolean(1) }
            }
        }

        return statement.first()
    }

    fun getUser(login: String): User? {
        val statement = database.useConnection { connection ->

            connection.prepareStatement("select * from users where login = ?").use { statement ->
                statement.setString(1, login)
                statement.executeQuery().asIterable().map { User(it.getString("login"), it.getString("password")) }
                }
            }
        return statement.firstOrNull()
    }
}