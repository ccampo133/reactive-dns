package me.ccampo.reactivedns

import com.amazonaws.services.lambda.runtime.Context
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

/**
 * @author Chris Campo
 */
class MainHandlerTest {

    @Test
    // TODO: mock out the AWS layer -ccampo 2016-06-23
    fun testMainHandler() {
        val handler = MainHandler()
        val orderJson = "{\"domain\":\"foobar123\", \"ip\":\"127.0.0.2\"}"
        val inStream = ByteArrayInputStream(orderJson.toByteArray(StandardCharsets.UTF_8))
        val outStream = ByteArrayOutputStream()
        handler.handleRequest(inStream, outStream, mock(Context::class.java))
    }
}
