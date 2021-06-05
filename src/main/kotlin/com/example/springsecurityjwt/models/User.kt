package com.example.springsecurityjwt.models

import com.example.springsecurityjwt.models.User.Role.USER
import com.example.springsecurityjwt.models.User.Status.ACTIVE
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true, nullable = false, updatable = false)
    @field:PositiveOrZero
    val id: Long = 0L,

    @Column(nullable = false, updatable = false)
    @field:NotBlank
    val firstName: String,

    @Column(nullable = false, updatable = false)
    @field:NotBlank
    val lastName: String,

    @Column(unique = true, nullable = false)
    @field:NotBlank
    @field:Email
    val email: String,

    @Column(unique = true, nullable = false, updatable = false)
    @field:NotBlank
    private val username: String,

    @Column(nullable = false)
    @field:NotBlank
    private val password: String,

    @Enumerated(STRING)
    @Column(nullable = false)
    val role: Role = USER,

    @Enumerated(STRING)
    @Column(nullable = false)
    val status: Status = ACTIVE,

    @Basic
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    val creationDate: LocalDateTime = LocalDateTime.now(),

    @Basic
    @UpdateTimestamp
    @Column(nullable = false)
    val lastUpdate: LocalDateTime = LocalDateTime.now()
) : UserDetails {
    enum class Role {
        USER,
        ADMIN;

        @JsonValue
        override fun toString(): String = name.lowercase()
    }

    enum class Status {
        ACTIVE,
        INACTIVE;

        @JsonValue
        override fun toString(): String = name.lowercase()
    }

    constructor(
        firstName: String,
        lastName: String,
        email: String,
        username: String,
        password: String
    ) : this(
        0,
        firstName,
        lastName,
        email,
        username,
        password
    )

    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
        SimpleGrantedAuthority(
            role.toString()
        )
    )

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean = false

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean = false

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean = false

    @JsonIgnore
    override fun isEnabled(): Boolean = status == ACTIVE
}
