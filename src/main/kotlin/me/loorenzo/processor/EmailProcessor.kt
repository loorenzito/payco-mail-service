package me.loorenzo.processor

import io.vertx.core.eventbus.Message
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import me.loorenzo.cases.SendEmailUseCase
import me.loorenzo.gateway.EmailEventPublisherImpl
import me.loorenzo.gateway.EmailWithValidation
import me.loorenzo.model.Email
import me.loorenzo.model.EmailStatus
import me.loorenzo.service.EmailService
import me.loorenzo.strategy.validator.EmailValidator
import me.loorenzo.strategy.validator.ValidationResult
import org.eclipse.microprofile.reactive.messaging.Incoming

@ApplicationScoped
class EmailProcessor @Inject constructor(
    private val emailService: EmailService,
    private val emailValidator: EmailValidator,
    private val emailEventPublisher: EmailEventPublisherImpl,

    private val sendEmailUseCase: SendEmailUseCase
) {

    @Incoming("email-sent")
    fun processEmail(emailMessage: Message<Email>) {
        val email = emailMessage.body()

        val validationResult = emailValidator.validateEmail(email)

        if (validationResult is ValidationResult.Success) {
            email.status = EmailStatus.VALIDATED
        } else {
            email.status = EmailStatus.FAILED
        }

        emailEventPublisher.publishEmailValidated(EmailWithValidation(email, validationResult))
    }

    @Incoming("email-validated")
    fun processEmailResult(emailMessage: Message<EmailWithValidation>) {
        val emailWithValidation = emailMessage.body()

        if (emailWithValidation.result is ValidationResult.Success) {
            val email = emailWithValidation.email

            emailService.saveEmail(email)
            sendEmailUseCase.sendEmail(emailWithValidation.email)
        }
    }
}