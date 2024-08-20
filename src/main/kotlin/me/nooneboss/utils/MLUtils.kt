package me.nooneboss.utils

import me.nooneboss.data.logs.ActionLog
import me.nooneboss.data.questionnaire.Questionnaire
import java.io.File

object MLUtils{
    fun writeLogsToCSV(logs: List<ActionLog>, filePath: String) {
        val file = File(filePath)
        file.printWriter().use { out ->
            out.println("timestamp|uuid|action|context")
            logs.forEach { log ->
                out.println("${log.timestamp}|${log.uuid}|${log.action}|${log.context}")
            }
        }
    }

    fun writeQuestionnairesToCSV(questionnaires: List<Questionnaire>, filePath: String) {
        val file = File(filePath)
        file.printWriter().use { out ->
            out.println("uuid|s|m|t|r")
            questionnaires.forEach { questionnaire ->
                out.println("${questionnaire.user_uuid}|${questionnaire.s}|${questionnaire.m}|${questionnaire.t}|${questionnaire.r}")
            }
        }
    }
}
