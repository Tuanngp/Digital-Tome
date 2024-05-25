//package com.fpt.swp391.group6.DigitalTome.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class UserConfiguration {
//
//    @Bean
//    @Autowired
//    public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
//        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
//        userDetailsManager.setUsersByUsernameQuery("Select username, password, status from account where username = ?");
//        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT account.username as username, role.name as role FROM account " +
//                "inner join role on role.id = account.role_id " +
//                "where account.username = ? ");
//        return userDetailsManager;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(
//                configurer -> configurer
//                        .requestMatchers(HttpMethod.GET, "/test").hasRole("ADMIN")
//        );
//
//        http.httpBasic(Customizer.withDefaults());
//        http.csrf(csrf -> csrf.disable());
//        return http.build();
//
//    }
//}
