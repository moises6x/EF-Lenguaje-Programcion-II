// src/main/java/ef/edu/cibertec/gestion/clientes/config/SecurityConfig.java
package ef.edu.cibertec.gestion.clientes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  // Cadena para APIs
	@Bean @Order(1)
	SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
	  http
	    .securityMatcher("/api/**")
	    // ⬇️ Ignora CSRF para TODAS las rutas /api/**
	    .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
	    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
	    .formLogin(form -> form.disable())
	    .httpBasic(basic -> basic.disable());
	  return http.build();
	}

  // Cadena para la web (vistas)
  @Bean @Order(2)
  SecurityFilterChain webChain(HttpSecurity http) throws Exception {
    http
    .authorizeHttpRequests(auth -> auth
    		  .requestMatchers(
    		    "/", "/index",
    		    "/login",
    		    "/registro",
    		    "/css/**", "/js/**", "/img/**", "/webjars/**", "/favicon.ico"
    		  ).permitAll()
    		  .anyRequest().permitAll() // en dev
    		)
      .csrf(csrf -> csrf.disable())
      // 👇 en lugar de deshabilitar formLogin, dile cuál es tu página:
      .formLogin(form -> form.loginPage("/login").permitAll())
      .httpBasic(basic -> basic.disable());
    return http.build();
  }

}

