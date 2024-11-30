//package com.retailedge.filter;
//
//import com.retailedge.service.user.UserService;
//import com.retailedge.utils.user.JWTUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Autowired
//    ApplicationContext context;
//
////    @Override
////    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
////        String authHeader = request.getHeader("Authorization");
////        String token = null;
////        String username = null;
////
////        if (authHeader != null && authHeader.startsWith("Bearer ")) {
////            token = authHeader.substring(7);
////            username = jwtUtil.extractUserName(token);
////        }
////
////        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////            UserDetails userDetails = context.getBean(UserService.class).loadUserByUsername(username);
////            if (jwtUtil.validateToken(token, userDetails)) {
////                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
////                authToken.setDetails(new WebAuthenticationDetailsSource()
////                        .buildDetails(request));
////                SecurityContextHolder.getContext().setAuthentication(authToken);
////            }
////        }
////
////        filterChain.doFilter(request, response);
////    }
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        String username = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            System.out.println("Authorization header found");
//            token = authHeader.substring(7);
//            username = jwtUtil.extractUserName(token);
//            System.out.println("Extracted username: " + username);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Lazily load UserService here
//            UserService userService = context.getBean(UserService.class);
//            UserDetails userDetails = userService.loadUserByUsername(username);
//            if (jwtUtil.validateToken(token, userDetails)) {
//                System.out.println("Token is valid for user: " + username);
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            } else {
//                System.out.println("Token validation failed for user: " + username);
//            }
//        } else {
//            System.out.println("Username is null or authentication already set");
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
//

package com.retailedge.filter;

import com.retailedge.repository.authentication.TokenBlacklistRepository;
import com.retailedge.service.user.UserService;
import com.retailedge.utils.user.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;
    // Define a list of public URLs that should bypass JWT filter
    private static final List<String> PUBLIC_URLS = List.of(
            "/api/auth/login",            // Login endpoint
            "/api/auth/forget-password",
            "/api/register",         // Registration endpoint
            "/swagger-ui/**",        // Swagger UI
            "/swagger-resources/**", // Swagger resources
            "/v3/api-docs/**"        // API docs
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        // Skip JWT filtering for public URLs
        if (isPublicUrl(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract the token

            Boolean tokenBlacklistService = tokenBlacklistRepository.existsByToken(token);
            if (tokenBlacklistService) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is blacklisted. Access denied.");
                return;
            }

            username = jwtUtil.extractUserName(token); // Extract username from token
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Lazily load UserService
            UserService userService = context.getBean(UserService.class);
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Helper method to check if the URL is in the list of public URLs
    private boolean isPublicUrl(String requestUri) {
        return PUBLIC_URLS.stream().anyMatch(requestUri::startsWith);
    }
}

