package com.edital.fullstack.domain.repository;

import com.edital.fullstack.domain.entity.Album;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, UUID> {
  Page<Album> findByArtistId(UUID artistId, Pageable pageable);
}
