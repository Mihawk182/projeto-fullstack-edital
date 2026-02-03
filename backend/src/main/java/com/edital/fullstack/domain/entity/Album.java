package com.edital.fullstack.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "album")
public class Album {
  @Id
  private UUID id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "artist_id", nullable = false)
  private Artist artist;

  @Column(nullable = false, length = 200)
  private String title;

  @Column(name = "cover_object_key", length = 400)
  private String coverObjectKey;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public Album() {}

  public Album(UUID id, Artist artist, String title, String coverObjectKey, Instant createdAt, Instant updatedAt) {
    this.id = id;
    this.artist = artist;
    this.title = title;
    this.coverObjectKey = coverObjectKey;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getCoverObjectKey() {
    return coverObjectKey;
  }

  public void setCoverObjectKey(String coverObjectKey) {
    this.coverObjectKey = coverObjectKey;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
