import { Client, IMessage } from "@stomp/stompjs";
import { notificationFacade } from "../facades/notificationFacade";

type AlbumCreatedEvent = {
  albumId: string;
  artistId: string;
  title: string;
  createdAt: string;
};

class AlbumSocket {
  private client?: Client;
  private isActive = false;

  connect() {
    if (this.isActive) {
      return;
    }

    const brokerURL = import.meta.env.VITE_WS_URL ?? "ws://localhost:8080/ws";
    this.client = new Client({
      brokerURL,
      reconnectDelay: 3000,
      debug: () => {}
    });

    this.client.onConnect = () => {
      this.client?.subscribe("/topic/albums", (message: IMessage) => {
        try {
          const payload = JSON.parse(message.body) as AlbumCreatedEvent;
          notificationFacade.add(`Novo album: ${payload.title}`);
        } catch {
          notificationFacade.add("Novo album cadastrado.");
        }
      });
    };

    this.client.onStompError = () => {
      notificationFacade.add("Falha na conexao de notificacoes.");
    };

    this.isActive = true;
    this.client.activate();
  }

  disconnect() {
    if (!this.client) {
      this.isActive = false;
      return;
    }
    this.client.deactivate();
    this.client = undefined;
    this.isActive = false;
  }
}

export const albumSocket = new AlbumSocket();
