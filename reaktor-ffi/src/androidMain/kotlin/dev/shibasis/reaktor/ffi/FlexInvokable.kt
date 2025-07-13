@file:Suppress("KotlinJniMissingFunction")

package dev.shibasis.reaktor.ffi

import dev.shibasis.reaktor.ffi.payload.functionName
import dev.shibasis.reaktor.ffi.payload.toFlexPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object Tester: Invokable {
    init {
        System.loadLibrary("FlatInvokerFFI")
    }
    external fun test(): Int

    external fun testHermes(): Int

    override fun invokeSync(payload: ByteArray): Long {
        val invokation = payload.toFlexPayload()
        val fnName = invokation.functionName
        invokation[3].type
        val a = invokation[3].toInt()
        val b = invokation[4].toInt()

        val result = when(fnName) {
            "add" -> a + b
            "sub" -> a - b
            "mul" -> a * b
            "div" -> a / b
            else -> throw IllegalArgumentException("Function not found")
        }

        return result.toLong()
    }

    override fun invokeAsync(payload: ByteArray): Flow<Long> {
        return flow { emit(-1) }
    }
}