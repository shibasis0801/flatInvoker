package dev.shibasis.reaktor.media

import kotlin.js.Promise
import dev.shibasis.reaktor.core.server.cloudflare.*
import js.promise.catch
import web.file.File


@JsExport
fun fetch(request: Request<Any, Any>, env: Env, ctx: ExecutionContext) = Promise { resolve, reject ->
    val url = URL(request.url)
    val key = url.pathname.substringAfter("/")

    when(request.method) {
        "GET" -> {
            val file = env.FILE.get(key)
            file.then {
                it?.apply {
                    val headers = Headers().apply {
                        set("etag", etag)
                    }
                    writeHttpMetadata(headers)
                    resolve(Response(body, object: ResponseInit {
                        override var headers = headers
                    }))

                } ?: throw NullPointerException("File not found")
            }.catch {
                resolve(Response(it.message))
            }

        }
        "POST" -> {
            request.formData().then {
                resolve(Response("Uploaded file $key"))
                Post(header = "2", content = "3", image = "4")
                val data = it.get("data")

                if (data == null)
                    resolve(Response("Failed to upload file $key"))
                println("Data is $data")

                val image = it.get("image")
                if (image == null)
                    resolve(Response("Failed to upload file $key"))
                println("Image is $image")

                val imageFile = image as File
                resolve(Response("Uploaded file $key"))
                imageFile.arrayBufferAsync().then { buffer ->
                    println("File $key length is ${buffer.byteLength}")
                    env.FILE.put(key, buffer)
                }.then {
                    resolve(Response("Uploaded file $key"))
                }.catch(reject)
            }
        }
        else -> reject(Error("Method not supported"))
    }
}
