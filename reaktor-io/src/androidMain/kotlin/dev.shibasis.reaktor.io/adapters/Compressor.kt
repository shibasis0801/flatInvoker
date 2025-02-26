package dev.shibasis.reaktor.io.adapters

import android.app.Activity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class AndroidCompressionAdapter: CompressionAdapter<Unit>(Unit) {
    override fun compress(request: CompressionRequest): CompressionResponse? {
        return try {
            val byteStream = ByteArrayOutputStream()
            GZIPOutputStream(byteStream)
                .bufferedWriter(StandardCharsets.UTF_8)
                .use { it.write(request.data) }

            val compressedBytes = byteStream.toByteArray()
            CompressionResponse(Base64.encode(compressedBytes))
        } catch (e: Exception) { null }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun decompress(request: DecompressionRequest): DecompressionResponse? {
        return try {
            val compressedBytes = Base64.Default.decode(request.base64EncodedString)

            val byteStream = ByteArrayInputStream(compressedBytes)
            val string = GZIPInputStream(byteStream)
                .bufferedReader(StandardCharsets.UTF_8)
                .use { it.readText() }

            DecompressionResponse(string)
        } catch (e: Exception) { null }
    }
}
