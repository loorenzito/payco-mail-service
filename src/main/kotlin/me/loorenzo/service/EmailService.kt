package me.loorenzo.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import me.loorenzo.model.Email
import me.loorenzo.repository.EmailRepository
import java.time.LocalDate

@ApplicationScoped
class EmailService @Inject constructor (
    private val emailRepository: EmailRepository
) {

    @Transactional
    fun saveEmail(email: Email): Email {
        return emailRepository.saveEmail(email)
    }

    fun findEmailById(id: Long): Email? {
        return emailRepository.findEmailById(id)
    }

    fun findEmailsByRecipient(recipient: String): List<Email> {
        return emailRepository.findEmailsByRecipient(recipient)
    }

    fun findEmailsByDate(date: LocalDate): List<Email> {
        return emailRepository.findEmailsByDate(date)
    }
}