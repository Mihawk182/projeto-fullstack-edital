package com.edital.fullstack.domain.service;

import com.edital.fullstack.domain.entity.Album;
import com.edital.fullstack.domain.repository.AlbumRepository;
import com.edital.fullstack.domain.repository.ArtistRepository;
import com.edital.fullstack.websocket.AlbumEventPublisher;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
  private final AlbumRepository repository;
  private final ArtistRepository artistRepository;
  private final AlbumEventPublisher eventPublisher;

  public AlbumService(
      AlbumRepository repository,
      ArtistRepository artistRepository,
      AlbumEventPublisher eventPublisher
  ) {
    this.repository = repository;
    this.artistRepository = artistRepository;
    this.eventPublisher = eventPublisher;
  }

  public Page<Album> list(UUID artistId, Pageable pageable) {
    if (artistId == null) {
      return repository.findAll(pageable);
    }
    return repository.findByArtistId(artistId, pageable);
  }

  public Album get(UUID id) {
    return repository.findById(id).orElseThrow();
  }

  public Album create(UUID artistId, String title) {
    var artist = artistRepository.findById(artistId).orElseThrow();
    var now = Instant.now();
    var album = new Album(UUID.randomUUID(), artist, title, null, now, now);
    var saved = repository.save(album);
    eventPublisher.publishCreated(saved);
    return saved;
  }

  public Album update(UUID id, String title) {
    var album = get(id);
    album.setTitle(title);
    album.setUpdatedAt(Instant.now());
    return repository.save(album);
  }

  public Album updateCover(UUID id, String coverObjectKey) {
    var album = get(id);
    album.setCoverObjectKey(coverObjectKey);
    album.setUpdatedAt(Instant.now());
    return repository.save(album);
  }
}
