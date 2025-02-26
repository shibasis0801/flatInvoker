package dev.shibasis.reaktor.navigation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.shibasis.reaktor.navigation.navigation.Navigator
import dev.shibasis.reaktor.ui.Theme

@Composable
actual fun BackHandlerContainer(
    modifier: Modifier,
    enabled: Boolean,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        BackHandler(enabled) {
            onBack()
        }
        content()
    }
}