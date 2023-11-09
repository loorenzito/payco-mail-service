package me.loorenzo.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import me.loorenzo.model.Email
import java.time.LocalDate

interface EmailRepository {
    fun saveEmail(email: Email): Email

    fun findEmailById(id: Long): Email?
    fun findEmailsByRecipient(recipient: String): List<Email>

    fun findEmailsByDate(date: LocalDate): List<Email>
}

@ApplicationScoped
class EmailRepositoryImpl : PanacheRepository<Email>, EmailRepository {
    override fun saveEmail(email: Email): Email {
        persist(email)
        return email
    }

    override fun findEmailById(id: Long): Email? {
        return findById(id)
    }

    override fun findEmailsByRecipient(recipient: String): List<Email> {
        return list("recipient", recipient)
    }

    override fun findEmailsByDate(date: LocalDate): List<Email> {
        return list("date", date)
    }
}