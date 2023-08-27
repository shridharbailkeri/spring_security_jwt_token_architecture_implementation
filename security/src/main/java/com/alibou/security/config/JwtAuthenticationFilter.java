package com.alibou.security.config;

import com.alibou.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization Header Bearer token
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); // is any of the above then just continue to filter chain return means dont do anything
            return;
        }

        // if Bearer token present under Authorization header, then split by space and give me the second part
        String token = authorizationHeader.split(" ")[1].trim(); // trim() and then remove spaces around it
        if (!jwtUtil.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        // if token is valid we can tell spring security that this user is safe you can authenticate this user

        String username = jwtUtil.getUsername(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        // next comes memorization part or you can copy from somewhere else
        // rememebr is login part for UsernamePasswordAuthenticationToken() we gace username and password as parameters
        // but to tell/give spring security that you can authenticate this user we use same thing there should be somekind of difference
        // so go inside UsernamePasswordAuthenticationToken class there are 2 constructors one of themm we used above username and password
        // this constructor setAuthenticated(false) this is what we use at login endpoint because user was not authenticated yet
        // but now below we use second constructor which setAuthenticated(true), which takes principal credentials and authorities
        // this is what we use now, spring security context will know that its an authenticated user
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        // now I give it to spring security context
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // this is a copy and paste part
        SecurityContextHolder.getContext().setAuthentication(authToken); // now we have authentication in security context

        filterChain.doFilter(request, response); // continue to do your filter in the filter chain

        // if hello end point gets called we will go through this flow written above

    }
}
