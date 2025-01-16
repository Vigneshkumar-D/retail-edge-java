package com.retailedge.configuration;

import com.retailedge.filter.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

import static org.springframework.security.config.Customizer.withDefaults;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

//     @Autowired
//     private JwtFilter jwtFilter;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         return http
//                 .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
//                 .cors(withDefaults()) // Allow CORS with default settings
//                 .sessionManagement(session -> session
//                         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Make session stateless
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers(
//                                 "/swagger-ui/**",
//                                 "/swagger-resources/**",
//                                 "/v3/api-docs/**",
//                                 "/api/auth/**",
//                                 "/public/**",
//                                 "/ws/**",
//                                 "/api/oee-excel/**"
//                         ).permitAll() // Allow public access to specific endpoints
//                         .anyRequest().authenticated() // All other requests require authentication
//                 )
//                 .httpBasic(withDefaults())
//                 .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before the auth filter
//                 .headers(headers -> headers
//                         .frameOptions(frame -> frame.deny())
//                         .contentSecurityPolicy(policy -> policy
//                                 .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src https://*; child-src 'none';")
//                         )
//                         .referrerPolicy(policy -> policy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
//                         .httpStrictTransportSecurity(sec -> sec.includeSubDomains(true).maxAgeInSeconds(31536000))
//                 )
//                 .build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AccessDeniedHandler accessDeniedHandler() {
//         return new CustomAccessDeniedHandler();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//         return authenticationConfiguration.getAuthenticationManager();
//     }
// }


@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(csrf -> csrf.disable())
//                .cors(withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/static/**", "/public/**", "/css/**", "/js/**", "/images/**").permitAll()
//                        .requestMatchers(
//                                "/swagger-ui/**",
//                                "/swagger-resources/**",
//                                "/v3/api-docs/**",
//                                "/api/auth/**",
//                                "/public/**",
//                                "/ws/**",
//                                "/login/**",
//                                "/forget-password",
//                                "/reset-password"
//                        ).permitAll()
//                        .requestMatchers("/").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults())
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .headers(headers -> headers
//                        .frameOptions(frame -> frame.deny())
//                        .contentSecurityPolicy(policy -> policy
//                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src https://*; child-src 'none';")
//                        )
//                        .referrerPolicy(policy -> policy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
//                        .httpStrictTransportSecurity(sec -> sec.includeSubDomains(true).maxAgeInSeconds(31536000))
//                )
//                .build();
//    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .securityMatcher("/api/**", "/swagger-ui/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**",
                                "/swagger-resources/*",
                                "/api/auth/**",
                                "/login/",
                                "/forget-password",
                                "/reset-password",
                                "/v3/api-docs/**").permitAll()
                        .requestMatchers("/public/**","/api/ws/**").permitAll()
                        .anyRequest().authenticated()
                )
                .cors(withDefaults())
                .httpBasic(withDefaults())
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentSecurityPolicy(policy -> policy
                                .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'; img-src https://*; child-src 'none';")
                        )
                        .referrerPolicy(policy -> policy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
                        .httpStrictTransportSecurity(sec -> sec.includeSubDomains(true).maxAgeInSeconds(31536000))
                )
                .build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}



