package com.example.fitfuture.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        // Aggiungi gli header CORS
        response.setHeader("Access-Control-Allow-Origin", "*"); // Consente tutte le origini
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, Authorization, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.setHeader("Access-Control-Allow-Credentials", "true"); // Abilita l'accesso alle credenziali
        response.setHeader("Access-Control-Max-Age", "3600"); // Cache degli header preflight per 1 ora

        response.setHeader("Vary", "");

        // Gestione delle richieste preflight (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return; // Rispondi subito alla richiesta OPTIONS
        }

        // Prosegui con il filtro successivo
        filterChain.doFilter(request, response);
    }
}
