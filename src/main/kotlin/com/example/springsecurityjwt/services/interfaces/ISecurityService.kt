package com.example.springsecurityjwt.services.interfaces

import com.example.springsecurityjwt.requests.SignUpRequest
import com.example.springsecurityjwt.responses.SignUpResponse

interface ISecurityService {
    fun signUp(signUpRequest: SignUpRequest): SignUpResponse
}
