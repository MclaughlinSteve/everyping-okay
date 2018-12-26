import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.io.InputStream

val mapper = jacksonObjectMapper()

fun Any.toJsonString(): String = mapper.writeValueAsString(this)


val slackUrl = System.getenv("SLACK_URL")
    ?: throw IllegalArgumentException("Missing SLACK_URL env variable")

val heartbeatUrl = System.getenv("HEARTBEAT_URL")
    ?: throw IllegalArgumentException("Missing HEARTBEAT_URL env variable")

fun main() {
    while(true) {
        performHeartbeatCheck()
        Thread.sleep(2000)
    }
}

fun handleLambda(input: InputStream) {
    performHeartbeatCheck()
}

private fun performHeartbeatCheck() {
    val heartbeatResult = checkHealth()
    when (heartbeatResult) {
        is Result.Success -> Unit
        is Result.Failure -> postAlert()
    }
}

private fun checkHealth(): Result<String, FuelError> {
    val (_, _, result) = heartbeatUrl.httpGet().responseString()
    return result
}

private fun postAlert() {
    data class Body(val text: String)
    val headers = mapOf("content-type" to "application/json")
    val body = Body("---ALERT--- The specified server is currently not responding.")
    slackUrl.httpPost().body(body.toJsonString()).header(headers).responseString()
    println("The server is down")
}
