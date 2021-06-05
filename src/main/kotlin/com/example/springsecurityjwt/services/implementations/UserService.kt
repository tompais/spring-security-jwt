package com.example.springsecurityjwt.services.implementations

import com.example.springsecurityjwt.dao.interfaces.IUserDAO
import com.example.springsecurityjwt.models.User
import com.example.springsecurityjwt.services.interfaces.IUserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userDAO: IUserDAO
) : IUserService {
    override fun createUser(user: User): User = userDAO.createUser(user)

    override fun loadUserByUsername(username: String): UserDetails = userDAO.getUserByUsername(username)
}
