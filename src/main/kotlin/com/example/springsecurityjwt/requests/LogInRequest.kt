package com.example.springsecurityjwt.requests

import javax.validation.constraints.NotBlank

class LogInRequest(
    @field:NotBlank
    val username: String,

    @field:NotBlank
    val password: String
)
