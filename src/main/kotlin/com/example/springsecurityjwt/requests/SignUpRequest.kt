package com.example.springsecurityjwt.requests

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class SignUpRequest(
    @field:NotBlank
    val firstName: String,

    @field:NotBlank
    val lastName: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String
)
