package com.radhika.tradepilot.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.AuthorityUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println(authHeader);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {

                String email = jwtUtil.extractEmail(token);

                if (jwtUtil.validateToken(token, email)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    AuthorityUtils.NO_AUTHORITIES
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (JwtException e) {
                System.out.println("Invalid JWT Token");
            }
        }

        filterChain.doFilter(request, response);
    }
}