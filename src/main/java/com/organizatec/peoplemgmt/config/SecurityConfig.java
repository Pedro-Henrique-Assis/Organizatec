package com.organizatec.peoplemgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança da aplicação utilizando Spring Security.
 *
 * Define os usuários em memória, senhas, perfis de acesso (roles) e as
 * regras de autorização para as rotas (URLs) do sistema.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define os usuários da aplicação que serão armazenados em memória.
     *
     * Cria três perfis de usuários distintos: RH, Recepção e Segurança,
     * cada um com suas credenciais e roles específicas.
     *
     * @return um {@link InMemoryUserDetailsManager} com os usuários pré-configurados.
     */
    @Bean
    InMemoryUserDetailsManager users() {
        UserDetails rh = User.withUsername("rh").password("{noop}rh123").roles("RH").build();
        UserDetails recepcao = User.withUsername("recepcao").password("{noop}rec123").roles("RECEPCAO").build();
        UserDetails seg = User.withUsername("seg").password("{noop}seg123").roles("SEGURANCA").build();
        return new InMemoryUserDetailsManager(rh, recepcao, seg);
    }

    /**
     * Configura as regras de autorização de acesso para as rotas HTTP.
     *
     * Define quais perfis de usuário (roles) podem acessar cada parte do sistema.
     * Rotas públicas como a de login e arquivos estáticos são permitidas para todos.
     *
     * @param http o objeto {@link HttpSecurity} para configurar as regras.
     * @return um {@link SecurityFilterChain} construído com as regras aplicadas.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
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
