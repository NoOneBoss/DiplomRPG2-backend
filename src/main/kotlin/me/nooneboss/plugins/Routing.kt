package me.nooneboss.plugins

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nooneboss.Systems
import me.nooneboss.data.auth.User
import me.nooneboss.data.chat.ChatLog
import me.nooneboss.data.logs.ActionLog
import me.nooneboss.data.logs.LogRequest
import me.nooneboss.data.questionnaire.Questionnaire
import me.nooneboss.utils.MLUtils.writeLogsToCSV
import me.nooneboss.utils.MLUtils.writeQuestionnairesToCSV
import org.jetbrains.annotations.Debug
import java.io.File
import java.util.*

fun Application.configureRouting() {
        routing {
            get("/serverstatus") {
                call.respondText("Server is running", status = HttpStatusCode.OK)
            }

            post("/questionnaire") {
                val formParameters = call.receiveParameters()
                val questionnaire = Questionnaire(formParameters["user_uuid"]!!, formParameters["s"]!!.toInt(), formParameters["m"]!!.toInt(), formParameters["t"]!!.toInt(), formParameters["r"]!!.toInt())

                Systems.questionnaireSystem.add(questionnaire)
                call.respond(HttpStatusCode.OK, "Questionnaire logged successfully")
            }

            authenticate {
                get("/player") {
                    val user = call.principal<User>()
                    if (user == null) call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    else call.respond(Gson().toJson(Systems.authSystem.getUser(user.login)))
                }

                get("/questionnaires") {
                    val file = File("ml/questionnaire.csv")

                    val user = call.principal<User>()
                    if (user == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    } else if(user.login == "nooneboss") {
                        writeQuestionnairesToCSV(Systems.questionnaireSystem.fetch(), "ml/questionnaire.csv")
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "ml/questionnaire.csv")
                                .toString()
                        )
                        call.respondFile(file)
                    }
                }

                post("/chat") {
                    val user = call.principal<User>()
                    val formParameters = call.receiveParameters()
                    val chatLog = ChatLog(formParameters["user_uuid"]!!, formParameters["timestamp"]!!, formParameters["message"]!!, formParameters["chat_type"]!!)

                    if (user == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    } else {
                        Systems.chatLogSystem.logMessage(chatLog)
                        call.respond(HttpStatusCode.OK, "Chat message logged successfully")
                    }
                }

                post("/log"){
                    val user = call.principal<User>()
                    val logs = call.receive<ActionLog>()

                    if (user == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    } else {
                        Systems.logSystem.logMessage(logs)
                        call.respond(HttpStatusCode.OK, "Logs for ${user.login} loaded successfully")
                    }
                }

                post("/logs"){
                    val user = call.principal<User>()
                    val logs = call.receive<LogRequest>()

                    if (user == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    } else {
                        Systems.logSystem.logMessages(logs)
                        call.respond(HttpStatusCode.OK, "Logs for ${user.login} loaded successfully, ${logs.logs.size}")
                        println("Logs for ${user.login} loaded successfully, ${logs.logs.size}")
                    }
                }

                get("/logs") {
                    val file = File("ml/logs.csv")

                    val user = call.principal<User>()
                    if (user == null) {
                        call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
                    } else if(user.login == "nooneboss") {
                        writeLogsToCSV(Systems.logSystem.fetchLogs(), "ml/logs.csv")
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, "ml/logs.csv")
                                .toString()
                        )
                        call.respondFile(file)
                    }
                }
            }
        }
}
