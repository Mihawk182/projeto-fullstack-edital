package com.edital.fullstack.web;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
  private final JdbcTemplate jdbcTemplate;

  public HealthController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/liveness")
  public Map<String, String> liveness() {
    return Map.of("status", "UP");
  }

  @GetMapping("/readiness")
  public ResponseEntity<Map<String, String>> readiness() {
    try {
      jdbcTemplate.queryForObject("select 1", Integer.class);
      return ResponseEntity.ok(Map.of("status", "UP"));
    } catch (Exception ex) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(Map.of("status", "DOWN"));
    }
  }
}
