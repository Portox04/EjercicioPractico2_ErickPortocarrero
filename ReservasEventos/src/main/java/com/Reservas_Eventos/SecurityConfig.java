/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Reservas_Eventos;

/**
 *
 * @author porto
 */
import com.Reservas_Eventos.service.UsuarioDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    public SecurityConfig(UsuarioDetailsService usuarioDetailsService) {
        this.usuarioDetailsService = usuarioDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ⚡ CAMBIO RÁPIDO: SIN BCrypt (solo para desarrollo)
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {

            boolean esAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            boolean esOrganizador = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZADOR"));

            if (esAdmin) {
                response.sendRedirect("/");
            } else if (esOrganizador) {
                response.sendRedirect("/");
            } else {
                response.sendRedirect("/");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/registro/**", "/recuperar/**",
                        "/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/usuarios/**", "/roles/**").hasRole("ADMIN")
                .requestMatchers("/eventos/guardar", "/eventos/eliminar", "/eventos/editar/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR")
                .requestMatchers("/eventos/listado")
                .hasAnyRole("ADMIN", "ORGANIZADOR", "CLIENTE")
                .requestMatchers("/consultas/**")
                .hasAnyRole("ADMIN", "ORGANIZADOR")
                .anyRequest().authenticated()
                )
                .userDetailsService(usuarioDetailsService)
                .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler())
                .failureUrl("/login?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                )
                .exceptionHandling(ex -> ex
                .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }
}
