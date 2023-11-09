package me.loorenzo.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Email(
    @Id
    val id: Long? = null,
    val recipient: String,
    val subject: String,
    val content: String,
    val strategy: String,
    var status: EmailStatus = EmailStatus.SENT
) {
    constructor() : this(null, "", "", "", "")
}

enum class EmailStatus {
    SENT,
    VALIDATED,
    FAILED
}