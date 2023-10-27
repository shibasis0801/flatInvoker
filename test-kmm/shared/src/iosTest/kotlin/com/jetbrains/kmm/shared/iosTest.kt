package com.jetbrains.kmm.shared

import com.jetbrains.kmm.shared.Platform
import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("iOS"), "Check iOS is mentioned")
        assertTrue(Platform().data == 12, "Able to call C++")
        println(Platform().data)
    }
}