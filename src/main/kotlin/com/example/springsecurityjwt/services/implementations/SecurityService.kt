package com.example.springsecurityjwt.services.implementations

import com.example.springsecurityjwt.models.User
import com.example.springsecurityjwt.requests.SignUpRequest
import com.example.springsecurityjwt.responses.SecurityResponse
import com.example.springsecurityjwt.services.interfaces.ISecurityService
import com.example.springsecurityjwt.services.interfaces.IUserService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
@Transactional
class SecurityService(
    private val userService: IUserService,
    private val passwordEncoder: PasswordEncoder
) : ISecurityService {
    private fun createUser(signUpRequest: SignUpRequest) = try {
        userService.createUser(
            User(
                firstName = signUpRequest.firstName,
                lastName = signUpRequest.lastName,
                email = signUpRequest.email,
                username = signUpRequest.username,
                password = passwordEncoder.encode(signUpRequest.password)
            )
        )
    } catch (e: DataIntegrityViolationException) {
        throw ResponseStatusException(CONFLICT, "The user already exists.", e)
    }

    private fun buildSignUpResponse(createdUser: User) = SecurityResponse(
        id = createdUser.id,
        firstName = createdUser.firstName,
        lastName = createdUser.lastName,
        email = createdUser.email,
        username = createdUser.username,
        role = createdUser.role
    )

    override fun signUp(signUpRequest: SignUpRequest): SecurityResponse = buildSignUpResponse(createUser(signUpRequest))
}
