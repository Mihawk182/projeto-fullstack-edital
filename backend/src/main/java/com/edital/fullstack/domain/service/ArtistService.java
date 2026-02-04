package com.edital.fullstack.domain.service;

import com.edital.fullstack.domain.entity.Artist;
import com.edital.fullstack.domain.repository.ArtistRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
  private final ArtistRepository repository;

  public ArtistService(ArtistRepository repository) {
    this.repository = repository;
  }

  public Page<Artist> list(String search, Pageable pageable) {
    if (search == null || search.isBlank()) {
      return repository.findAll(pageable);
    }
    return repository.findByNameContainingIgnoreCase(search, pageable);
  }

  public Artist get(UUID id) {
    return repository.findById(id).orElseThrow();
  }

  public Artist create(String name) {
    var now = Instant.now();
    var artist = new Artist(UUID.randomUUID(), name, now, now);
    return repository.save(artist);
  }

  public Artist update(UUID id, String name) {
    var artist = get(id);
    artist.setName(name);
    artist.setUpdatedAt(Instant.now());
    return repository.save(artist);
  }
}
