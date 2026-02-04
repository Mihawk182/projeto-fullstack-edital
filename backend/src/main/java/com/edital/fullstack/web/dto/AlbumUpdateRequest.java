package com.edital.fullstack.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AlbumUpdateRequest(@NotBlank String title) {}
