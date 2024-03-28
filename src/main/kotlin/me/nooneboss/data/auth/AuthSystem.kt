package me.nooneboss.data.auth

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.nooneboss.data.User
import org.ktorm.database.Database
import java.util.*

object AuthSystem {
    private fun createHikariDataSource(url: String, driver: String) = HikariDataSource(
        HikariConfig()
        .apply {
            driverClassName = driver
            jdbcUrl = url
            maximumPoolSize = 3
            username="postgres"
            password="postgres"
            validate()
        }
    )

    val database = Database.connect(createHikariDataSource(url = "jdbc:postgresql://localhost:5432/postgres", driver = "org.postgresql.Driver"))


    fun login(login: String, password: String) : Boolean {
        val statement = database.useConnection {
            it.prepareStatement("select login_user(?,?)")
        }

        statement.setString(1, login)
        statement.setString(2, password)

        val result = statement.executeQuery()
        result.next()

        return result.getBoolean(1)
    }

    fun register(login: String, password: String) : Boolean{
        val statement = database.useConnection {
            it.prepareStatement("select register_user(?,?)")
        }

        statement.setString(1, login)
        statement.setString(2, password)

        val result = statement.executeQuery()
        result.next()

        return result.getBoolean(1)
    }

    fun getUser(userLogin: String): User? {
        val statement = database.useConnection {
            it.prepareStatement("select * from users where login = ?")
        }
        statement.setString(1, userLogin)
        val result = statement.executeQuery()
        return if(result.next()) {
            User(
                result.getString("login"),
                result.getString("password")
            )
        } else {
            null
        }
    }
}