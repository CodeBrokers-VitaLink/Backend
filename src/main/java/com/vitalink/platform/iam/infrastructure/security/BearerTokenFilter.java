package com.vitalink.platform.iam.infrastructure.security;

import com.vitalink.platform.iam.infrastructure.persistence.jpa.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class BearerTokenFilter extends OncePerRequestFilter {
  private final AuthSessionRepository sessions;
  private final UserRepository users;

  public BearerTokenFilter(AuthSessionRepository sessions, UserRepository users) {
    this.sessions = sessions;
    this.users = users;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws java.io.IOException, ServletException {
    var header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ") && header.length() < 2048) {
      sessions
          .findById(TokenCodec.hash(header.substring(7)))
          .filter(session -> !session.expired())
          .flatMap(session -> users.findById(session.getUserId()))
          .ifPresent(
              user ->
                  SecurityContextHolder.getContext()
                      .setAuthentication(
                          new UsernamePasswordAuthenticationToken(
                              user,
                              null,
                              List.of(
                                  new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))));
    }
    chain.doFilter(request, response);
  }
}
