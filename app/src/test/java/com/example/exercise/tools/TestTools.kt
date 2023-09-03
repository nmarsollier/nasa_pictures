package com.example.exercise.tools

fun sleepForResult(tries: Int = 10, function: () -> Unit) {
    var iterations = 0
    var error: Throwable? = null
    while (iterations < tries) {
        error = try {
            function()
            return
        } catch (er: Error) {
            er
        } catch (ex: Exception) {
            ex
        }
        iterations++
        Thread.sleep(300)
    }
    error?.let {
        throw (it)
    }
}