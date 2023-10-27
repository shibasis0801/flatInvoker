package com.jetbrains.kmm.shared

import com.google.flatbuffers.kotlin.ArrayReadBuffer
import com.google.flatbuffers.kotlin.FlexBuffersBuilder
import com.google.flatbuffers.kotlin.ReadBuffer
import com.google.flatbuffers.kotlin.getRoot
import com.google.flatbuffers.kotlin.value
import kotlin.test.Test
import kotlin.test.assertEquals

// kotlin/com/google/flatbuffers/kotlin/FlexBuffersTest.kt
/*
I had to change few things in the
 */
class CalculatorTest {

    @Test
    fun testSum() {
        assertEquals(3, Calculator.sum(1, 2))
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun flatHelloWorld() {
        val helloWorld = "Hello Flatbuffers"
        val startBytes = FlexBuffersBuilder().apply {
            put(helloWorld)
        }.finish()

        val result = getRoot(startBytes).toString()
        assertEquals(result, helloWorld)

    }

}