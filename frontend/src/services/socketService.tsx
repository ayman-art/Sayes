import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class WebSocketService {
  private client: Client;

  constructor(webSocketUrl: string) {
    this.client = new Client({
      webSocketFactory: () => new SockJS(webSocketUrl),
      debug: (str) => console.log(str),
      reconnectDelay: 5000, // Automatically reconnect after 5 seconds
    });
  }

  connect(onConnect: () => void, onError: (error: string) => void) {
    this.client.onConnect = onConnect;
    this.client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
      onError(frame.body);
    };
    this.client.activate();
  }

  disconnect() {
    if (this.client.active) {
      this.client.deactivate();
    }
  }

  subscribe(topic: string, callback: (message: IMessage) => void) {
    if (this.client.connected) {
      return this.client.subscribe(topic, (message) => {
        callback(message);
      });
    }
    throw new Error('WebSocket client is not connected.');
  }

  unsubscribe(subscriptionId: string) {
    this.client.unsubscribe(subscriptionId);
  }
}

export default WebSocketService;
