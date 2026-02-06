package com.edital.fullstack.web;

import com.edital.fullstack.regional.RegionalSyncResult;
import com.edital.fullstack.regional.RegionalSyncService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regionais")
public class RegionalController {
  private final RegionalSyncService syncService;

  public RegionalController(RegionalSyncService syncService) {
    this.syncService = syncService;
  }

  @PostMapping("/sync")
  public RegionalSyncResult sync() {
    return syncService.sync();
  }
}
