package com.example.whatsthere.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class UserData(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val imageUrl: String? = ""
){

    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )

}

data class ChatData(
    val chatId: String? = "",
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
)

data class ChatUser(
    val userId: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val number: String? = ""
)

data class Message(
    val sentBy: String? = "",
    val message: String? = "",
    val timeStamp: String? = ""
)

data class Status(
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val timestamp: Long? = null
)

@JsonClass(generateAdapter = true)
data class CatImage(
    @Json(name = "id") val id: String,
    @Json(name = "url") val url: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int

)