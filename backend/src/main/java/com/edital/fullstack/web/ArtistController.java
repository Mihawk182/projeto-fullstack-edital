package com.edital.fullstack.web;

import com.edital.fullstack.domain.service.ArtistService;
import com.edital.fullstack.web.dto.ArtistCreateRequest;
import com.edital.fullstack.web.dto.ArtistResponse;
import com.edital.fullstack.web.dto.ArtistUpdateRequest;
import com.edital.fullstack.web.mapper.ArtistMapper;
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
@RequestMapping("/api/v1/artists")
public class ArtistController {
  private final ArtistService service;

  public ArtistController(ArtistService service) {
    this.service = service;
  }

  @GetMapping
  public Page<ArtistResponse> list(
    @RequestParam(required = false) String search,
    @PageableDefault(size = 10) Pageable pageable
  ) {
    return service.listWithCounts(search, pageable);
  }

  @GetMapping("/{id}")
  public ArtistResponse get(@PathVariable UUID id) {
    return service.getWithCount(id);
  }

  @PostMapping
  public ArtistResponse create(@Valid @RequestBody ArtistCreateRequest request) {
    var artist = service.create(request.name());
    return ArtistMapper.toResponse(artist, 0);
  }

  @PutMapping("/{id}")
  public ArtistResponse update(@PathVariable UUID id, @Valid @RequestBody ArtistUpdateRequest request) {
    var artist = service.update(id, request.name());
    return ArtistMapper.toResponse(artist, service.countAlbums(id));
  }
}
