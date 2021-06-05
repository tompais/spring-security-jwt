package com.example.springsecurityjwt.services.interfaces

import com.example.springsecurityjwt.models.User
import org.springframework.security.core.userdetails.UserDetailsService

interface IUserService : UserDetailsService {
    fun createUser(user: User): User
}
