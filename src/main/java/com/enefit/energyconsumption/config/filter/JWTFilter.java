package com.enefit.energyconsumption.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import com.enefit.energyconsumption.config.security.AppUserDetailsService;
import com.enefit.energyconsumption.config.util.JWTUtil;

/**
 * A custom JWT authentication filter that intercepts HTTP requests to validate
 * and process JWT tokens.
 *
 * @author Keshani
 * @since 2025/05/31
 */

@Component
public class JWTFilter
        extends OncePerRequestFilter {

    private final AppUserDetailsService appUserDetailsService;
    private final JWTUtil jwtUtil;

    public JWTFilter(final AppUserDetailsService appUserDetailsService, JWTUtil jwtUtil) {
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationToken  = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if(authorizationToken != null && authorizationToken.startsWith("Bearer ")) {
            jwt = authorizationToken.substring(7);
            username =jwtUtil.extractUsername(jwt);
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails detail = appUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.isTokenValid(jwt, username)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return  path.contains("login");
    }
}
