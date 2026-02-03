package com.edital.fullstack.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AlbumCreateRequest(@NotNull UUID artistId, @NotBlank String title) {}
