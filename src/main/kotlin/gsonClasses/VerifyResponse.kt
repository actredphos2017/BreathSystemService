package gsonClasses

import com.google.gson.annotations.SerializedName

data class VerifyResponse(
    @SerializedName("response")
    val response: String,
    @SerializedName("success")
    val success: String
) {
    companion object {
        fun success(response: String = ""): VerifyResponse = VerifyResponse(response, "true")
        fun failed(response: String = ""): VerifyResponse = VerifyResponse(response, "false")
    }
}