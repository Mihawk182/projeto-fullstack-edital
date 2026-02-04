package com.edital.fullstack.domain.repository;

import com.edital.fullstack.domain.entity.Artist;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
  Page<Artist> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
