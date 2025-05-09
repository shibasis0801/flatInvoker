package dev.shibasis.reaktor.auth.api

import dev.shibasis.reaktor.auth.User
import dev.shibasis.reaktor.core.network.StatusCode
import dev.shibasis.reaktor.io.network.http
import dev.shibasis.reaktor.io.network.postJson
import dev.shibasis.reaktor.io.service.BaseRequest
import dev.shibasis.reaktor.io.service.BaseResponse
import dev.shibasis.reaktor.io.service.RequestHandler
import dev.shibasis.reaktor.io.service.Service
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class SignInRequest(
    val idToken: String,
    val appId: String,
    val providerId: String = "Google",
    override val headers: MutableMap<String, String> = mutableMapOf(),
    override val queryParams: MutableMap<String, String> = mutableMapOf(),
    override val pathParams: MutableMap<String, String> = mutableMapOf()
): BaseRequest

@Serializable
sealed class SignInResponse(
    override var statusCode: StatusCode = StatusCode.OK,
    override val headers: MutableMap<String, String> = mutableMapOf()
): BaseResponse {
    @Serializable
    data class Success(val user: User, val profile: JsonElement): SignInResponse(StatusCode.OK)

    @Serializable
    sealed class Failure(private val hack: StatusCode): SignInResponse(hack) {
        @Serializable
        data object InvalidGoogleIdToken: Failure(StatusCode.BAD_REQUEST)

        @Serializable
        data object InvalidAppId: Failure(StatusCode.BAD_REQUEST)

        @Serializable
        class ServerError(val message: String): Failure(StatusCode.INTERNAL_SERVER_ERROR)
    }
}


abstract class AuthService: Service() {
    abstract val signIn: RequestHandler<SignInRequest, SignInResponse>
}

class AuthServiceClient(baseUrl: String): AuthService() {
    override val signIn = PostHandler("${baseUrl}/auth/sign-in") {
        http.postJson<SignInRequest, SignInResponse>(route, it).getOrNull() ?: SignInResponse.Failure.ServerError("Unknown Error")
    }
}