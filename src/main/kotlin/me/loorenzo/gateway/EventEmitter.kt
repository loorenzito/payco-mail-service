package me.loorenzo.gateway

import io.vertx.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped
import me.loorenzo.model.Email
import me.loorenzo.strategy.validator.ValidationResult

data class EmailWithValidation(val email: Email, val result: ValidationResult)

interface EmailEventPublisher {
    fun publishEmailSent(email: Email)
    fun publishEmailValidated(emailWithValidation: EmailWithValidation)
}

@ApplicationScoped
class EmailEventPublisherImpl(private val eventBus: EventBus) : EmailEventPublisher {
    override fun publishEmailSent(email: Email) {
        eventBus.publish("email-sent", email)
    }

    override fun publishEmailValidated(emailWithValidation: EmailWithValidation) {
        eventBus.publish("email-validated", emailWithValidation)
    }
}
