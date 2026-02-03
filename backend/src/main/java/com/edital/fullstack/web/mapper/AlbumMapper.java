package com.edital.fullstack.web.mapper;

import com.edital.fullstack.domain.entity.Album;
import com.edital.fullstack.web.dto.AlbumResponse;

public final class AlbumMapper {
  private AlbumMapper() {}

  public static AlbumResponse toResponse(Album album) {
    return new AlbumResponse(
      album.getId(),
      album.getArtist().getId(),
      album.getTitle(),
      album.getCoverObjectKey()
    );
  }
}
