package me.loorenzo.resource

import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import me.loorenzo.gateway.EmailEventPublisherImpl
import me.loorenzo.model.Email

@Path("/v1/email")
class EmailResource @Inject constructor(
    private val emailEventPublisher: EmailEventPublisherImpl,
) {

    @POST
    @Path("/send")
    fun sendEmail(email: Email) {
        emailEventPublisher.publishEmailSent(email)
    }
}