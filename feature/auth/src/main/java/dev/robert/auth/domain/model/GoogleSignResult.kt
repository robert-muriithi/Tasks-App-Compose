package dev.robert.auth.domain.model

data class GoogleSignResult(
    val data: GoogleUser? = null,
    val errorMsg: String? = null
)
