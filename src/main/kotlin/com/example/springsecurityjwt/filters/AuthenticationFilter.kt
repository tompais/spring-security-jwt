package com.example.springsecurityjwt.filters

import com.example.springsecurityjwt.models.User
import com.example.springsecurityjwt.requests.LogInRequest
import com.example.springsecurityjwt.responses.SecurityResponse
import com.example.springsecurityjwt.utils.SecurityConstant.EXPIRATION_TIME_IN_MINUTES
import com.example.springsecurityjwt.utils.SecurityConstant.SECURITY_KEY
import com.example.springsecurityjwt.utils.toDate
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.security.Key
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val mapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse
    ): Authentication = try {
        mapper.readValue<LogInRequest>(req.inputStream).let { logInRequest ->
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    logInRequest.username,
                    logInRequest.password,
                    emptyList()
                )
            )
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }

    private fun HttpServletResponse.writeLogInResponse(user: User) {
        addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
        writer.use { writer ->
            writer.write(
                mapper.writeValueAsString(
                    SecurityResponse(
                        id = user.id,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        email = user.email,
                        username = user.username,
                        role = user.role
                    )
                )
            )
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        val creationDate = LocalDateTime.now()
        val expirationDate = LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MINUTES)
        val key: Key = Keys.hmacShaKeyFor(SECURITY_KEY.toByteArray())
        val user = auth.principal as User
        val claims = Jwts.claims(
            mapOf(
                "role" to user.role.toString()
            )
        )
        val token: String =
            Jwts.builder()
                .setSubject(user.username)
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(creationDate.toDate())
                .setExpiration(expirationDate.toDate())
                .compact()
        res.addHeader("X-Auth-Token", token)
        res.writeLogInResponse(user)
    }
}
