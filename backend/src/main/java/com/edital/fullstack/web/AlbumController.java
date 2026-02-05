package com.edital.fullstack.web;

import com.edital.fullstack.domain.service.AlbumService;
import com.edital.fullstack.storage.MinioStorageService;
import com.edital.fullstack.web.dto.AlbumCreateRequest;
import com.edital.fullstack.web.dto.AlbumResponse;
import com.edital.fullstack.web.dto.AlbumUpdateRequest;
import com.edital.fullstack.web.mapper.AlbumMapper;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/albums")
public class AlbumController {
  private final AlbumService service;
  private final MinioStorageService storageService;

  public AlbumController(AlbumService service, MinioStorageService storageService) {
    this.service = service;
    this.storageService = storageService;
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

  @PostMapping(value = "/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, String> uploadCover(@PathVariable UUID id, @RequestParam("file") MultipartFile file) throws Exception {
    var objectKey = storageService.upload(file.getOriginalFilename(), file.getInputStream(), file.getSize(), file.getContentType());
    service.updateCover(id, objectKey);
    var url = storageService.presignedUrl(objectKey, Duration.ofMinutes(30));
    return Map.of("coverObjectKey", objectKey, "coverUrl", url);
  }

  @GetMapping("/{id}/cover")
  public Map<String, String> getCover(@PathVariable UUID id) throws Exception {
    var album = service.get(id);
    if (album.getCoverObjectKey() == null) {
      return Map.of("coverUrl", "");
    }
    var url = storageService.presignedUrl(album.getCoverObjectKey(), Duration.ofMinutes(30));
    return Map.of("coverUrl", url);
  }
}
