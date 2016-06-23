package me.ccampo.reactivedns.guice

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.TypeLiteral
import java.io.InputStream
import java.io.OutputStream

/**
 * Uses Guice to get a [RequestHandler] which takes an input of type [I]
 * and returns an output of type [O]. The input and output objects are
 * converted to POJOs using a custom Jackson [ObjectMapper], which must
 * be provided when this class is implemented.
 *
 * This class exists to get around the limitations of AWS Lambda's
 * primitive JSON serialization and deserialization. By processing the
 * raw [InputStream] and [OutputStream]s ourselves, we have much more
 * control over the serialization/deserialization processes.
 */
abstract class JacksonGuiceRequestHandler<I, O>(val inputClass: Class<I>) : RequestStreamHandler {
    protected abstract val injector: Injector
    protected abstract val objectMapper: ObjectMapper

    override fun handleRequest(input: InputStream?, output: OutputStream?, context: Context?) {
        @Suppress("UNCHECKED_CAST")
        val handler = injector.getInstance(Key.get(object : TypeLiteral<RequestHandler<*, *>>() {})) as
                RequestHandler<I, O>
        val inObject = objectMapper.readValue(input, inputClass)
        val outObject = handler.handleRequest(inObject, context)
        objectMapper.writeValue(output, outObject)
    }
}
