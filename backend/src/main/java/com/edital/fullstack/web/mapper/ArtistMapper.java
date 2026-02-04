package com.edital.fullstack.web.mapper;

import com.edital.fullstack.domain.entity.Artist;
import com.edital.fullstack.web.dto.ArtistResponse;

public final class ArtistMapper {
  private ArtistMapper() {}

  public static ArtistResponse toResponse(Artist artist) {
    return new ArtistResponse(artist.getId(), artist.getName());
  }
}
