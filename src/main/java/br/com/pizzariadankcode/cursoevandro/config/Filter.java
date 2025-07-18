package br.com.pizzariadankcode.cursoevandro.config;

import br.com.pizzariadankcode.cursoevandro.usuario.Usuario;
import br.com.pizzariadankcode.cursoevandro.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Filter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = buscarToken(request);
        if (token != null) {
            var usuarioLogin = tokenService.buscaUsuarioToken(token);
            UserDetails usuario = usuarioRepository.findByLogin(usuarioLogin);

            var autenticador = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(autenticador);
        }
        filterChain.doFilter(request, response);
    }

    private String buscarToken(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");

        if (authorization != null) {
            return authorization.replace("Bearer ", "");
        }
        return null;
    }
}
