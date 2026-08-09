package ru.n1fex.kinto.filter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.n1fex.kinto.dto.ErrorResponse;
import ru.n1fex.kinto.dto.JwtAuthentication;
import ru.n1fex.kinto.service.SessionService;
import ru.n1fex.kinto.util.JwtProvider;
import ru.n1fex.kinto.util.JwtUtils;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtProvider jwtProvider;
    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(servletRequest);

        if (token == null) {
            filterChain.doFilter(servletRequest, response);
            return;
        }

        if (jwtProvider.validateAccessToken(token)) {

            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtInfoToken = JwtUtils.generateAuthentication(claims);

            if (!sessionService.refreshExists(jwtInfoToken.getSid())) {
                accessDenyRequest(servletRequest, response, "Войдите в аккаунт");
            } else {
                jwtInfoToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);

                filterChain.doFilter(servletRequest, response);
            }

        } else {
            accessDenyRequest(servletRequest, response, "Ошибка обработки JWT токена");
        }

    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/login")
                || path.startsWith("/auth/register")
                || path.startsWith("/auth/refresh");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private void accessDenyRequest(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse er = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message);


        response.resetBuffer();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter pw = response.getWriter();
        pw.write(mapper.writeValueAsString(er));
        pw.flush();
    }
}
