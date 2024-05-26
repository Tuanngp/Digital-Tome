package com.fpt.swp391.group6.DigitalTome.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class UserConfiguration {

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/css/**", "/js/**", "/images/**", "/icons/**", "/vendor/**").permitAll()
                                .requestMatchers("/users").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/profile/**").authenticated()
                                .requestMatchers("/**").permitAll()

                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/home", true)
                                .permitAll()

                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails admin = User
                .withUsername("tuan@gmail.com")
                .password("{noop}tuan123")
                .roles("admin")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}
