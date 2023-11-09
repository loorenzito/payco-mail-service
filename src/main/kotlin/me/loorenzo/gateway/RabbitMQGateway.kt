package me.loorenzo.gateway

import io.quarkus.arc.DefaultBean
import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithName
import io.vertx.core.Vertx
import io.vertx.rabbitmq.RabbitMQClient
import io.vertx.rabbitmq.RabbitMQOptions
import jakarta.enterprise.inject.Produces
import jakarta.inject.Inject

@ConfigMapping(prefix = "quarkus.smallrye-reactive-messaging.amqp")
interface RabbitMQConfig {

    @WithName("host")
    fun host(): String

    @WithName("port")
    fun port(): Int

    @WithName("username")
    fun username(): String

    @WithName("password")
    fun password(): String
}

@DefaultBean
class RabbitMQGateway @Inject constructor (
    private val rabbitMQConfig: RabbitMQConfig
){

    @Produces
    fun rabbitMQClient(): RabbitMQClient {
        val options = RabbitMQOptions().apply {
            host = rabbitMQConfig.host()
            port = rabbitMQConfig.port()
            user = rabbitMQConfig.username()
            password = rabbitMQConfig.password()
        }

        val client = RabbitMQClient.create(Vertx.vertx(), options)
        if (!client.isConnected) client.start()

        return client
    }
}