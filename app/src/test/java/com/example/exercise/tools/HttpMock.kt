package com.example.exercise.tools

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert
import kotlin.concurrent.thread

open class ApiMockMapClass(
    val urlOverride: (baseUrl: String) -> Unit, val maps: Map<String, String>
) {
    var server: MockWebServer? = null
        set(value) {
            field?.shutdown()
            field = value?.also {
                thread {
                    // it.url must be called in background
                    urlOverride(value.url("/").toString())
                }
            }
        }

    fun mockTest() {
        stopMocking()
        server = mockWebServer {
            val path = it.requestUrl!!.encodedPath

            val resId = maps.keys.firstOrNull { key ->
                path.contains(key)
            }?.let { key ->
                Thread.sleep(1000)
                maps[key]
            }

            if (resId == null) {
                Thread.sleep(1000)
                send404()
            } else {
                Thread.sleep(1000)
                send200(resId)
            }
        }
    }

    /**
     * Send 200 with the Int resource as body
     */
    fun send200(resId: String): MockResponse {
        return MockResponse().setResponseCode(200).setBody(rawResToString(resId))
    }

    /**
     * Send 200 with the Int resource as body
     */
    fun send200(): MockResponse {
        return MockResponse().setResponseCode(200)
    }

    /**
     * Mock send 404
     */
    fun send404(): MockResponse {
        return MockResponse().setResponseCode(404)
    }

    fun send400(resId: String): MockResponse {
        return MockResponse().setResponseCode(400).setBody(rawResToString(resId))
    }

    fun rawResToString(r: String): String {
        return javaClass.getResourceAsStream("/$r")?.bufferedReader().use {
            it?.readText() ?: ""
        }
    }

    fun mock404ServerError() {
        stopMocking()
        server = mockWebServer {
            // return an error value
            send404()
        }
    }

    fun stopMocking() {
        server = null
    }
}


fun mockWebServer(dispatchFun: (request: RecordedRequest) -> MockResponse): MockWebServer {
    return MockWebServer().apply {
        dispatcher = (object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return dispatchFun(request)
            }
        })
    }
}

fun ApiMockMapClass.mockForUnitTest(
    validator: () -> Boolean
) {
    this.mockTest()
    sleepForResult {
        Assert.assertTrue(validator())
    }
}

fun ApiMockMapClass.mock404ServerErrorForUnit(
    validator: () -> Boolean
) {
    this.mock404ServerError()
    sleepForResult {
        Assert.assertTrue(validator())
    }
}

