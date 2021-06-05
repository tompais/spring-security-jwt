package com.example.springsecurityjwt.filters

import com.example.springsecurityjwt.utils.SecurityConstant.SECURITY_KEY
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.IOException
import io.jsonwebtoken.security.Keys
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(authenticationManager: AuthenticationManager) :
    BasicAuthenticationFilter(authenticationManager) {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        request.getHeader(AUTHORIZATION)?.let { header ->
            if (header.startsWith("Bearer ")) {
                SecurityContextHolder.getContext().authentication = authenticate(header.removePrefix("Bearer "))
            }
        }

        chain.doFilter(request, response)
    }

    private fun authenticate(token: String): UsernamePasswordAuthenticationToken? =
        Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECURITY_KEY.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body?.let { claims ->
                UsernamePasswordAuthenticationToken(
                    claims.subject,
                    null,
                    listOf(
                        SimpleGrantedAuthority(
                            claims.get("role", String::class.java)
                        )
                    )
                )
            }
}
