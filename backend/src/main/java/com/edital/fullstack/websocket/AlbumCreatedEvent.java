package com.edital.fullstack.websocket;

import java.time.Instant;
import java.util.UUID;

public record AlbumCreatedEvent(UUID albumId, UUID artistId, String title, Instant createdAt) {}
