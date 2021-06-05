package com.example.springsecurityjwt.dao.interfaces

import com.example.springsecurityjwt.models.User

interface IUserDAO {
    fun getUserByUsername(username: String): User
    fun createUser(user: User): User
}
