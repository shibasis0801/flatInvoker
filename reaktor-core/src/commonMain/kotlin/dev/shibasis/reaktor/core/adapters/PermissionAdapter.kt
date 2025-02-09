package dev.shibasis.reaktor.core.adapters

import dev.shibasis.reaktor.core.framework.Adapter
import dev.shibasis.reaktor.core.framework.Feature
import dev.shibasis.reaktor.core.framework.CreateSlot


sealed class PermissionResult {
    data object Granted: PermissionResult()
    sealed class Denied(): PermissionResult() {
        data class Unknown(val error: Error): Denied()
        data object Once: Denied()
        data object Forever: Denied()
    }
}

object Permission {
    val CAMERA = "CAMERA"
    val LOCATION = "LOCATION"
    val STORAGE = "STORAGE"
    val GALLERY = "GALLERY"
    val SPEECH_RECOGNITION = "SPEECH_RECOGINTION"
}

abstract class PermissionAdapter<Controller>(controller: Controller): Adapter<Controller>(controller) {
    // requestAll is binary yes or no for all permissions
    abstract suspend fun request(vararg permissions: String): Boolean

    // You can also override this method for more granular permission handling
    suspend fun requestOptional(vararg permissions: String): Map<String, PermissionResult> = hashMapOf()
}

var Feature.Permission by CreateSlot<PermissionAdapter<*>>()