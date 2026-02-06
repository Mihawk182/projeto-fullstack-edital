package com.edital.fullstack.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
  private static final int MAX_REQUESTS_PER_MINUTE = 10;
  private static final long WINDOW_MS = 60_000L;
  private static final Map<String, Deque<Long>> REQUESTS = new ConcurrentHashMap<>();

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    var path = request.getRequestURI();
    if (HttpMethod.OPTIONS.matches(request.getMethod())) {
      return true;
    }
    return path.startsWith("/api/v1/auth")
        || path.startsWith("/api/v1/health")
        || path.startsWith("/swagger")
        || path.startsWith("/api-docs")
        || path.startsWith("/ws");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var key = resolveKey(request);
    var now = Instant.now().toEpochMilli();
    var queue = REQUESTS.computeIfAbsent(key, k -> new ArrayDeque<>());

    synchronized (queue) {
      while (!queue.isEmpty() && now - queue.peekFirst() > WINDOW_MS) {
        queue.pollFirst();
      }
      if (queue.size() >= MAX_REQUESTS_PER_MINUTE) {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Rate limit excedido (10 req/min).\"}");
        return;
      }
      queue.addLast(now);
    }

    filterChain.doFilter(request, response);
  }

  private String resolveKey(HttpServletRequest request) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
      return "user:" + auth.getName();
    }
    return "ip:" + request.getRemoteAddr();
  }
}
