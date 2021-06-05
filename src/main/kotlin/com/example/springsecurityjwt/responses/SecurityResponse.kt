package com.example.springsecurityjwt.responses

import com.example.springsecurityjwt.models.User.Role
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class SecurityResponse(
    @field:Positive
    val id: Long,

    @field:NotBlank
    val firstName: String,

    @field:NotBlank
    val lastName: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val username: String,

    val role: Role
)
