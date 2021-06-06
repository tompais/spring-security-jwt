package com.example.springsecurityjwt.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {
    @GetMapping("/ping", produces = [TEXT_PLAIN_VALUE])
    @Operation(security = [SecurityRequirement(name = "bearer-key")])
    fun ping(): String = "pong"
}
