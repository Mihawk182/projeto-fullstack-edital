package com.edital.fullstack.regional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RegionalSyncService {
  private final RegionalRepository repository;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  private final String syncUrl;

  public RegionalSyncService(
      RegionalRepository repository,
      ObjectMapper objectMapper,
      @Value("${regional.sync-url}") String syncUrl
  ) {
    this.repository = repository;
    this.objectMapper = objectMapper;
    this.restTemplate = new RestTemplate();
    this.syncUrl = syncUrl;
  }

  public RegionalSyncResult sync() {
    JsonNode root = restTemplate.getForObject(syncUrl, JsonNode.class);
    var items = extractArray(root);

    Map<Integer, String> remote = new HashMap<>();
    for (JsonNode node : items) {
      Integer id = readInt(node, "id", "codigo");
      String nome = readString(node, "nome", "name", "denominacao");
      if (id != null && nome != null && !nome.isBlank()) {
        remote.put(id, nome);
      }
    }

    var now = Instant.now();
    int inserted = 0;
    int inactivated = 0;
    int updated = 0;

    Set<Integer> remoteIds = new HashSet<>(remote.keySet());
    for (Regional active : repository.findByAtivoTrue()) {
      if (!remoteIds.contains(active.getRegionalId())) {
        active.setAtivo(false);
        active.setUpdatedAt(now);
        repository.save(active);
        inactivated++;
      }
    }

    for (Map.Entry<Integer, String> entry : remote.entrySet()) {
      Integer id = entry.getKey();
      String nome = entry.getValue();
      var current = repository.findFirstByRegionalIdAndAtivoTrue(id).orElse(null);
      if (current == null) {
        repository.save(new Regional(id, nome, true, now, now));
        inserted++;
        continue;
      }
      if (!current.getNome().equals(nome)) {
        current.setAtivo(false);
        current.setUpdatedAt(now);
        repository.save(current);
        repository.save(new Regional(id, nome, true, now, now));
        updated++;
      }
    }

    return new RegionalSyncResult(inserted, inactivated, updated);
  }

  private Iterable<JsonNode> extractArray(JsonNode root) {
    if (root == null) {
      return java.util.List.of();
    }
    if (root.isArray()) {
      return root;
    }
    if (root.has("data") && root.get("data").isArray()) {
      return root.get("data");
    }
    if (root.has("items") && root.get("items").isArray()) {
      return root.get("items");
    }
    if (root.has("content") && root.get("content").isArray()) {
      return root.get("content");
    }
    return java.util.List.of();
  }

  private Integer readInt(JsonNode node, String... fields) {
    for (String field : fields) {
      if (node.has(field) && node.get(field).canConvertToInt()) {
        return node.get(field).asInt();
      }
    }
    return null;
  }

  private String readString(JsonNode node, String... fields) {
    for (String field : fields) {
      if (node.has(field) && node.get(field).isTextual()) {
        return node.get(field).asText();
      }
    }
    return null;
  }
}
