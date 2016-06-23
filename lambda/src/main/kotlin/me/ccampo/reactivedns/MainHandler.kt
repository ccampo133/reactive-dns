package me.ccampo.reactivedns

import com.amazonaws.services.lambda.runtime.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Guice
import com.google.inject.Injector
import me.ccampo.reactivedns.config.MainModule
import me.ccampo.reactivedns.guice.JacksonGuiceRequestHandler
import me.ccampo.reactivedns.model.DnsUpdateRequest
import me.ccampo.reactivedns.model.DnsUpdateResponse
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream

class MainHandler : JacksonGuiceRequestHandler<DnsUpdateRequest, DnsUpdateResponse>(DnsUpdateRequest::class.java) {

    companion object {
        val log = LoggerFactory.getLogger(MainHandler::class.java)
    }

    override val injector: Injector
        get() = Guice.createInjector(MainModule())

    override val objectMapper: ObjectMapper
        get() = injector.getInstance(ObjectMapper::class.java)

    override fun handleRequest(input: InputStream?, output: OutputStream?, context: Context?) {
        log.info("New request")
        super.handleRequest(input, output, context)
    }
}
