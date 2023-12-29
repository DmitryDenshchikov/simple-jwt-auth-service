package denshchikov.dmitry.app.filter;

import denshchikov.dmitry.app.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static String BEARER_PREFIX = "Bearer ";
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Claims claims;

        try {
            var authorization = extractAuthorizationHeader(request);
            var jwt = extractJwt(authorization);
            claims = jwtService.getClaims(jwt);
        } catch (ServletRequestBindingException | JwtException e) {
            logger.debug(e.getMessage());
            response.sendError(HttpStatus.FORBIDDEN.value());
            return;
        }

        var principal = claims.get("principal");
        var authorities = extractAuthorities(claims);
        var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var requestURI = request.getRequestURI();
        return "/auth/token".equals(requestURI);
    }

    private String extractAuthorizationHeader(HttpServletRequest request) throws ServletRequestBindingException {
        var authorisation = request.getHeader(AUTHORIZATION);

        if (authorisation == null) {
            throw new ServletRequestBindingException("'%s' header is missing".formatted(AUTHORIZATION));
        }

        if (!authorisation.startsWith(BEARER_PREFIX)) {
            var msg = "Malformed '%s' header. It should start with '%s'".formatted(AUTHORIZATION, BEARER_PREFIX);
            throw new ServletRequestBindingException(msg);
        }

        return authorisation;
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        var roles = claims.get("roles", Collection.class);

        if (roles == null) {
            return List.of();
        }

        if (roles instanceof Collection<?> r) {
            return r.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        } else {
            throw new JwtException("Roles should be a string collection, but the actual type is %s"
                    .formatted(roles.getClass().getCanonicalName()));
        }
    }

    private String extractJwt(String authorization) throws ServletRequestBindingException {
        var jwt = authorization.substring(BEARER_PREFIX.length());

        if (!StringUtils.hasText(jwt)) {
            throw new ServletRequestBindingException("JWT is not present in the request");
        }

        return jwt;
    }

}