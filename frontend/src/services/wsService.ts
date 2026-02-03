export class WebSocketService {
  private socket?: WebSocket;

  connect(url: string) {
    this.socket = new WebSocket(url);
  }

  disconnect() {
    this.socket?.close();
  }
}
