package com.example.exercise.tools

import io.mockk.every
import io.mockk.mockk
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import kotlin.test.assertEquals

fun buildOkHttpClientMock(testHelper: TestHttpServer) = mockk<OkHttpClient> {
    every { newCall(any()) } answers {
        val request = firstArg<Request>()
        mockk {
            val call = this
            every { enqueue(any()) } answers {
                val callback = firstArg<Callback>()
                callback.onResponse(call, testHelper.handleRequest(request))
            }
        }
    }
}

/**
 * Mock server calls into a local test web server
 *
 * We map the start of the url to an string result.
 *
 */
open class TestHttpServer {
    /**
     * String result is a resource json String resource
     * Or a number with the status code that we want to return, examples:
     *
     * The url does not have to be complete, this library checks that the current path contains the url
     *
     *      "/users/me" -> "users_me.json",   // Where users_me.json is a json result for 200
     *      "/users/stats" -> "500",          // 500 is the status code to return
     */
    private val maps = mutableMapOf<String, String>()

    /**
     * Set Answer Map
     */
    fun setAnswer(url: String, response: String) = maps.put(url, response)

    var requestCount = 0
    var lastRequest: Request? = null

    /**
     * Starts to mock
     */
    fun handleRequest(httpRequest: Request): Response {
        requestCount++
        lastRequest = httpRequest

        val path = httpRequest.url.encodedPath

        println("Http call handling $path")

        val resId = maps.keys.firstOrNull { key ->
            path == key
        }?.let { key ->
            maps[key]
        }

        val status = resId?.toIntOrNull()

        return when {
            status != null -> Response.Builder()
                .code(status)
                .request(httpRequest)
                .protocol(Protocol.HTTP_1_1)
                .message("Test error")
                .body("".toResponseBody("application/json".toMediaType()))
                .build()

            resId != null -> Response.Builder()
                .code(200)
                .request(httpRequest)
                .protocol(Protocol.HTTP_1_1)
                .message("Ok")
                .body(rawResToString(resId).toResponseBody("application/json".toMediaType()))
                .build()

            else -> throw Exception("Invalid HttpMock configuration for path $path")
        }
    }

    /**
     * Reads a raw string resource and returns the String
     */
    private fun rawResToString(r: String): String {
        return javaClass.getResourceAsStream("/$r")?.bufferedReader().use {
            it?.readText() ?: ""
        }
    }

    /**
     * Asserts current request count done on this server
     */
    fun assertRequestCount(count: Int) {
        val recordedRequests = requestCount
        assertEquals(count, recordedRequests)
    }

    /**
     * Checks that the last call was done for the given url, method and/or body.
     * Url can be just the beggin of the real url
     */
    fun assertLastCall(url: String, method: String? = null) {
        val recordedRequest = lastRequest!!

        assertEquals(url, recordedRequest.url.encodedPath.take(url.length))

        method?.let {
            assertEquals(it, recordedRequest.method)
        }
    }
}
