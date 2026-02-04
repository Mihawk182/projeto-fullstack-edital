package com.edital.fullstack.web.dto;

import java.util.UUID;

public record AlbumResponse(UUID id, UUID artistId, String title, String coverObjectKey) {}
