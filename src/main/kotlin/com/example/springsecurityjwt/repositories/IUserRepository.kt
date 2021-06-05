package com.example.springsecurityjwt.repositories

import com.example.springsecurityjwt.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface IUserRepository : JpaRepository<User, Long> {
    fun getUserByUsername(username: String): User
}
