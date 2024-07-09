package dev.robert.auth.domain.mappers

import dev.robert.auth.data.model.GoogleUserDto
import dev.robert.auth.domain.model.GoogleUser

fun GoogleUserDto.toGoogleUser() = GoogleUser(
    email = email,
    name = name,
    photoUrl = photoUrl,
    id = id
)
