package com.springboot.blog.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get Jwt token
        String token= getBearerToken(request);

        // get JWt token validate
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            //Get Username for token from jwttokenprovider
            String username= jwtTokenProvider.getUsername(token);
            //Get user details object for username
            UserDetails userDetails= userDetailsService.loadUserByUsername(username);
            //get object for UserpasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(request, response);


    }

    private String getBearerToken( HttpServletRequest request){
        String bearerToken= request.getHeader("Authorization");

        //Extract JWT token from the header
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());

        }
        return  null;
    }
}
