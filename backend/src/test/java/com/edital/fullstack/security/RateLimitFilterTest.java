package com.edital.fullstack.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class RateLimitFilterTest {
  private final RateLimitFilter filter = new RateLimitFilter();

  @BeforeEach
  void clearState() throws Exception {
    Field field = RateLimitFilter.class.getDeclaredField("REQUESTS");
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, ?> requests = (Map<String, ?>) field.get(null);
    requests.clear();
  }

  @Test
  void shouldSkipAuthAndHealth() {
    var auth = new MockHttpServletRequest("POST", "/api/v1/auth/login");
    var health = new MockHttpServletRequest("GET", "/api/v1/health/liveness");

    assertTrue(filter.shouldNotFilter(auth));
    assertTrue(filter.shouldNotFilter(health));
  }

  @Test
  void blocksAfterTenRequestsPerMinute() throws Exception {
    FilterChain chain = mock(FilterChain.class);

    for (int i = 0; i < 10; i++) {
      var req = new MockHttpServletRequest("GET", "/api/v1/artists");
      req.setRemoteAddr("127.0.0.1");
      var res = new MockHttpServletResponse();
      filter.doFilter(req, res, chain);
      assertEquals(200, res.getStatus());
    }

    var req = new MockHttpServletRequest("GET", "/api/v1/artists");
    req.setRemoteAddr("127.0.0.1");
    var res = new MockHttpServletResponse();
    filter.doFilter(req, res, chain);

    assertEquals(429, res.getStatus());
    assertFalse(res.getContentAsString().isBlank());
    verify(chain, times(10)).doFilter(org.mockito.Mockito.any(), org.mockito.Mockito.any());
  }
}
