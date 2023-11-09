package me.loorenzo.strategy

import io.quarkus.mailer.Mail
import io.quarkus.mailer.Mailer
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Any
import jakarta.enterprise.inject.Default
import jakarta.inject.Inject
import jakarta.inject.Named
import me.loorenzo.model.Email

interface EmailSendingStrategy {
    fun name(): String
    fun send(email: Email)
}

@ApplicationScoped
class AmazonEmailSendingStrategy : EmailSendingStrategy {

    @Inject
    @Any
    @Default
    @Named("ses")
    private lateinit var mailer: Mailer

    override fun name(): String {
        return "ses"
    }

    override fun send(email: Email) {
        mailer.send(
            Mail.withText(
                email.recipient,
                email.subject,
                email.content
            )
        )
    }
}
@ApplicationScoped
class SendGridEmailSendingStrategy : EmailSendingStrategy {

    @Inject
    @Any
    @Default
    @Named("sendgrid")
    private lateinit var mailer: Mailer

    override fun name(): String {
        return "sendgrid"
    }

    override fun send(email: Email) {
        mailer.send(
            Mail.withText(
                email.recipient,
                email.subject,
                email.content
            )
        )
    }
}