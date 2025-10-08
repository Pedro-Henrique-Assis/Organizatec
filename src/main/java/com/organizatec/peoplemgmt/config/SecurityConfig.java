package com.organizatec.peoplemgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    InMemoryUserDetailsManager users() {
        UserDetails rh = User.withUsername("rh").password("{noop}rh123").roles("RH").build();
        UserDetails recepcao = User.withUsername("recepcao").password("{noop}rec123").roles("RECEPCAO").build();
        UserDetails seg = User.withUsername("seg").password("{noop}seg123").roles("SEGURANCA").build();
        return new InMemoryUserDetailsManager(rh, recepcao, seg);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/styles.css", "/css/**", "/webjars/**").permitAll()
                        .requestMatchers("/employees/**").hasRole("RH")
                        .requestMatchers("/visitors/**").hasAnyRole("RECEPCAO","SEGURANCA","RH")
                        .requestMatchers("/contractors/**").hasAnyRole("RH","SEGURANCA")
                        .requestMatchers("/reports/**").hasAnyRole("RH","SEGURANCA")
                        .anyRequest().authenticated()
                ).formLogin(login -> login.permitAll())
                .logout(logout -> logout.permitAll());
        return http.build();
    }
}
