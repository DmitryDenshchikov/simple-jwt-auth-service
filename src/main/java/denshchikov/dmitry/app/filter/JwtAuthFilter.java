package denshchikov.dmitry.app.filter;

import denshchikov.dmitry.app.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        var authorisation = request.getHeader(AUTHORIZATION);

        if (authorisation == null) {
            throw new ServletRequestBindingException("'%s' header is missing".formatted(AUTHORIZATION));
        }

        if (!authorisation.startsWith(BEARER_PREFIX)) {
            var msg = "Malformed '%s' header. It should start with '%s'".formatted(AUTHORIZATION, BEARER_PREFIX);
            throw new ServletRequestBindingException(msg);
        }

        var jwt = authorisation.substring(BEARER_PREFIX.length());

        if (!StringUtils.hasText(jwt)) {
            throw new ServletRequestBindingException("JWT is not present in the request");
        }

        var claims = jwtService.getClaims(jwt);
        var principal = claims.get("principal");
        var roles = claims.get("roles", String[].class);

        var authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .toList();

        var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var requestURI = request.getRequestURI();
        return "/auth/token".equals(requestURI);
    }

}