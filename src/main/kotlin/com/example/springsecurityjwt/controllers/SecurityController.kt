package com.example.springsecurityjwt.controllers

import com.example.springsecurityjwt.requests.SignUpRequest
import com.example.springsecurityjwt.responses.SecurityResponse
import com.example.springsecurityjwt.services.interfaces.ISecurityService
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/security", consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
class SecurityController(
    private val securityService: ISecurityService
) {
    @PostMapping("/sign-up")
    @ResponseStatus(CREATED)
    fun signUp(@RequestBody @Valid signUpRequest: SignUpRequest): SecurityResponse =
        securityService.signUp(signUpRequest)
}
