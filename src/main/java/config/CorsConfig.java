package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")  //  Applica CORS a tutte le chiamate API
                        .allowedOrigins("")  //  Puoi specificare il dominio invece di ""
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // Metodi permessi
                        .allowedHeaders("*");  // Permettiamo tutti gli headers
            }
        };
    }
}