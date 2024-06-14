package com.fpt.swp391.group6.DigitalTome.config;

import com.fpt.swp391.group6.DigitalTome.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    private static final String[] PUBLIC_ENDPOINT = {"/**", "/index", "/home", "/register", "/forgotPassword", "/books-list", "/register-publisher"};

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    public SpringSecurity(CustomOAuth2UserService customOAuth2UserService, CustomUserDetailsService customUserDetailsService, CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/uploadbook/**").hasAnyRole("PUBLISHER")
                        .requestMatchers("/publisher/**").hasAnyRole("PUBLISHER", "ADMIN")
                        .requestMatchers("/admin/").hasAnyRole("ADMIN")
                        .requestMatchers("/admin/**").permitAll()
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers("/buypoint/**").authenticated()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers(PUBLIC_ENDPOINT).permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService))
                        .defaultSuccessUrl("/")
                        .failureUrl("/login")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/404"));
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
}