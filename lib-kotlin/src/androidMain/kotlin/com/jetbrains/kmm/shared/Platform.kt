package com.jetbrains.kmm.shared

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual val data: Int = Reaktor.readInt()
}

object Reaktor {
    external fun readInt(): Int
    init {
        System.loadLibrary("kmmFlatInvoker")
    }
}
