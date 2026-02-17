package com.mumbailocal.ticketservice.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

   
	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain)
	        throws ServletException, IOException {

	    String token = null;

	    // 1️⃣ Check Authorization header
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7);
	    }

	    // 2️⃣ If no header token, check cookies
	    if (token == null && request.getCookies() != null) {
	        for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
	            if ("jwt".equals(cookie.getName())) {
	                token = cookie.getValue();
	                break;
	            }
	        }
	    }

	    // 3️⃣ If token exists → validate
	    if (token != null) {
	        try {
	            Claims claims = JwtUtil.validateToken(token);
	            Long userId = claims.get("userId", Long.class);

	            UsernamePasswordAuthenticationToken authentication =
	                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

	            SecurityContextHolder.getContext().setAuthentication(authentication);

	        } catch (Exception e) {
	            // ❌ DO NOT block request
	            // just continue without authentication
	        }
	    }

	    // Always continue request
	    filterChain.doFilter(request, response);
	}
}