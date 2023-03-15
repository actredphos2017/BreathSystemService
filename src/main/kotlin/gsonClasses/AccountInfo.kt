package gsonClasses

import com.google.gson.annotations.SerializedName

data class AccountInfo(
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("id")
    val id: String
)