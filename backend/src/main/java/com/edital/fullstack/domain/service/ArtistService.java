package com.edital.fullstack.domain.service;

import com.edital.fullstack.domain.entity.Artist;
import com.edital.fullstack.domain.repository.AlbumRepository;
import com.edital.fullstack.domain.repository.ArtistRepository;
import com.edital.fullstack.web.dto.ArtistResponse;
import com.edital.fullstack.web.mapper.ArtistMapper;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistService {
  private final ArtistRepository repository;
  private final AlbumRepository albumRepository;

  public ArtistService(ArtistRepository repository, AlbumRepository albumRepository) {
    this.repository = repository;
    this.albumRepository = albumRepository;
  }

  public Page<ArtistResponse> listWithCounts(String search, Pageable pageable) {
    return list(search, pageable)
        .map(artist -> ArtistMapper.toResponse(artist, albumRepository.countByArtistId(artist.getId())));
  }

  public ArtistResponse getWithCount(UUID id) {
    var artist = get(id);
    return ArtistMapper.toResponse(artist, albumRepository.countByArtistId(id));
  }

  public long countAlbums(UUID artistId) {
    return albumRepository.countByArtistId(artistId);
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
