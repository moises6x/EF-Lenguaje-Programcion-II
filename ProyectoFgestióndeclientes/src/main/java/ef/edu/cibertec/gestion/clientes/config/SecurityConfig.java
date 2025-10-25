// src/main/java/ef/edu/cibertec/gestion/clientes/config/SecurityConfig.java
package ef.edu.cibertec.gestion.clientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())                 // desactiva CSRF para Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()   // 🔓 deja libres tus APIs
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());        // opcional (para pruebas)
        return http.build();
    }
}

