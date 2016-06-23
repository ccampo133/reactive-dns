package me.ccampo.reactivedns.config

import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.route53.AmazonRoute53Client
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import com.google.inject.name.Names
import me.ccampo.reactivedns.handler.DnsUpdateRequestHandler
import java.io.FileInputStream
import java.util.*

class MainModule : AbstractModule() {
    override fun configure() {
        Names.bindProperties(binder(), readProperties())
    }

    @Provides @Singleton
    fun requestHandler(@Named("zone.id") zoneId: String, @Named("zone.name") zoneName: String): RequestHandler<*, *> {
        return DnsUpdateRequestHandler(AmazonRoute53Client(), zoneId, zoneName)
    }

    @Provides @Singleton
    fun objectMapper() = ObjectMapper().registerModule(KotlinModule())

    fun readProperties() = Properties().apply {
        FileInputStream("src/main/resources/application.properties").use { fis -> load(fis) }
    }
}
