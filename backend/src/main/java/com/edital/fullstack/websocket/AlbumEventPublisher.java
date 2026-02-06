package com.edital.fullstack.websocket;

import com.edital.fullstack.domain.entity.Album;
import java.time.Instant;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class AlbumEventPublisher {
  private final SimpMessagingTemplate messagingTemplate;

  public AlbumEventPublisher(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  public void publishCreated(Album album) {
    var event = new AlbumCreatedEvent(
        album.getId(),
        album.getArtist().getId(),
        album.getTitle(),
        album.getCreatedAt() == null ? Instant.now() : album.getCreatedAt()
    );
    messagingTemplate.convertAndSend("/topic/albums", event);
  }
}
