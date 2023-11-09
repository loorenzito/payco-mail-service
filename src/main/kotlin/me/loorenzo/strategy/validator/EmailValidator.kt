package me.loorenzo.strategy.validator

import jakarta.enterprise.context.ApplicationScoped
import me.loorenzo.model.Email
import me.loorenzo.strategy.AmazonEmailSendingStrategy
import me.loorenzo.strategy.EmailSendingStrategy
import me.loorenzo.strategy.SendGridEmailSendingStrategy

interface FieldValidator {
    fun validate(value: String): Boolean
}

class EmailFormatValidator : FieldValidator {
    override fun validate(value: String): Boolean {
        return value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex())
    }
}

class RequiredFieldValidator : FieldValidator {
    override fun validate(value: String): Boolean {
        return value.isNotBlank()
    }
}

class MaxLengthValidator(private val maxLength: Int) : FieldValidator {
    override fun validate(value: String): Boolean {
        return value.length <= maxLength
    }
}

class MinLengthValidator(private val minLength: Int) : FieldValidator {
    override fun validate(value: String): Boolean {
        return value.length >= minLength
    }
}

class StrategyValidator(private val strategies: List<EmailSendingStrategy>) : FieldValidator {
    override fun validate(value: String): Boolean {
        return strategies.map {
            it.name()
        }.contains(value)
    }
}

sealed class ValidationResult {
    data class Success(val email: Email) : ValidationResult()
    data class Failed(val message: String) : ValidationResult()
}

@ApplicationScoped
class EmailValidator {

    private val recipientValidators = listOf(
        RequiredFieldValidator(),
        EmailFormatValidator()
    )

    private val subjectValidators = listOf(
        RequiredFieldValidator(),
        MaxLengthValidator(100)
    )

    private val contentValidators = listOf(
        RequiredFieldValidator(),
        MinLengthValidator(20)
    )

    private val strategyValidators = listOf(
        RequiredFieldValidator(),
        MinLengthValidator(4),
        MaxLengthValidator(8),
        StrategyValidator(listOf(AmazonEmailSendingStrategy(),
            SendGridEmailSendingStrategy()))
    )

    fun validateEmail(email: Email): ValidationResult {
        return when {
            validateField(email, email.recipient, recipientValidators) is ValidationResult.Failed -> {
                ValidationResult.Failed("Recipient validation failed")
            }

            validateField(email, email.subject, subjectValidators) is ValidationResult.Failed -> {
                ValidationResult.Failed("Subject validation failed")
            }

            validateField(email, email.content, contentValidators) is ValidationResult.Failed -> {
                ValidationResult.Failed("Content validation failed")
            }

            validateField(email, email.content, contentValidators) is ValidationResult.Failed -> {
                ValidationResult.Failed("Content validation failed")
            }

            validateField(email, email.strategy, strategyValidators) is ValidationResult.Failed -> {
                ValidationResult.Failed("Strategy validation failed")
            }

            else -> {
                ValidationResult.Success(email)
            }
        }
    }

    private fun validateField(email: Email, value: String, validators: List<FieldValidator>): ValidationResult {
        for (validator in validators) {
            if (!validator.validate(value)) {
                return ValidationResult.Failed("Validation failed for field: $value")
            }
        }
        return ValidationResult.Success(email)
    }
}