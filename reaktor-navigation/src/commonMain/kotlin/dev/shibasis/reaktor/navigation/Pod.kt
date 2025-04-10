package dev.shibasis.reaktor.navigation

import dev.shibasis.reaktor.navigation.route.Container
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module

interface Pod: KoinComponent {
    fun modules(): List<Module>
    fun container(): Container
}