package com.edital.fullstack.web;

import com.edital.fullstack.domain.service.AlbumService;
import com.edital.fullstack.web.dto.AlbumCreateRequest;
import com.edital.fullstack.web.dto.AlbumResponse;
import com.edital.fullstack.web.dto.AlbumUpdateRequest;
import com.edital.fullstack.web.mapper.AlbumMapper;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {
  private final AlbumService service;

  public AlbumController(AlbumService service) {
    this.service = service;
  }

  @GetMapping
  public Page<AlbumResponse> list(
    @RequestParam(required = false) UUID artistId,
    @PageableDefault(size = 10) Pageable pageable
  ) {
    return service.list(artistId, pageable).map(AlbumMapper::toResponse);
  }

  @PostMapping
  public AlbumResponse create(@Valid @RequestBody AlbumCreateRequest request) {
    return AlbumMapper.toResponse(service.create(request.artistId(), request.title()));
  }

  @PutMapping("/{id}")
  public AlbumResponse update(@PathVariable UUID id, @Valid @RequestBody AlbumUpdateRequest request) {
    return AlbumMapper.toResponse(service.update(id, request.title()));
  }
}
