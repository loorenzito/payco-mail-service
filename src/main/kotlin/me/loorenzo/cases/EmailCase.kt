package me.loorenzo.cases

import jakarta.enterprise.context.ApplicationScoped
import me.loorenzo.model.Email
import me.loorenzo.strategy.AmazonEmailSendingStrategy
import me.loorenzo.strategy.SendGridEmailSendingStrategy

@ApplicationScoped
class SendEmailUseCase {

    private val strategyMap = mapOf("amazon" to AmazonEmailSendingStrategy(),
        "sendgrid" to SendGridEmailSendingStrategy())

    fun sendEmail(email: Email) {
        val strategy = strategyMap[email.strategy]!!
        strategy.send(email)
    }
}