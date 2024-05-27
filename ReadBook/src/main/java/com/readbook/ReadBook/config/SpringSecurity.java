package com.readbook.ReadBook.config;

import com.readbook.ReadBook.securiry.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/users/**").hasAnyRole( "ADMIN")
                                .requestMatchers("/admin/**").hasAnyRole( "ADMIN")
                                .requestMatchers("/profile/**").authenticated()
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler((request, response, authentication) -> {
                                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                                        if (auth.getAuthority().equals("ROLE_ADMIN")) {
                                            response.sendRedirect("/admin");
                                            return;
                                        }
                                    }
                                    response.sendRedirect("/index");
                                })
                                .permitAll()
                ).oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint
                                                .userService(customOAuth2UserService))
                                .defaultSuccessUrl("/index")
                                .failureUrl("/login?error")
                                .permitAll())
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                ).exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/404")
                );;
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

}

