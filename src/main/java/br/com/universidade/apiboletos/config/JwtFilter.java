package br.com.universidade.apiboletos.config;

import br.com.universidade.apiboletos.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        String path = req.getRequestURI();

        // Liberar preflight OPTIONS
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(request, response);
            return;
        }

        // Liberar rotas que usam API key para autenticação (POST /boletos e PUT
        // /boletos/status/**)
        if (("POST".equalsIgnoreCase(req.getMethod()) && path.equals("/boletos")) ||
                ("PUT".equalsIgnoreCase(req.getMethod()) && path.startsWith("/boletos/status/"))) {
            chain.doFilter(request, response);
            return; // não exige token JWT para essas rotas
        }

        // Validação padrão JWT para outras rotas
        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente");
            return;
        }
        String token = authHeader.substring(7);
        try {
            if (!jwtUtil.validateToken(token)) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Token inválido ou expirado");
                return;
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        }
    }

}
