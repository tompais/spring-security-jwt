package com.example.springsecurityjwt.dao.implementations

import com.example.springsecurityjwt.dao.interfaces.IUserDAO
import com.example.springsecurityjwt.models.User
import com.example.springsecurityjwt.repositories.IUserRepository
import org.springframework.stereotype.Repository

@Repository
class UserDAO(
    private val userRepository: IUserRepository
) : IUserDAO {
    override fun getUserByUsername(username: String): User = userRepository.getUserByUsername(username)
    override fun createUser(user: User): User = userRepository.save(user)
}
