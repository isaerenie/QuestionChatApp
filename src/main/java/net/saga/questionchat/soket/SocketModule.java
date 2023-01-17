package net.saga.questionchat.soket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import net.saga.questionchat.model.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketModule {
    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onMessageReceived());
    }

    private DataListener<Message> onMessageReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendSocketMessage(data.getRoom(), "get_message",
                    senderClient, data.getMessage(), data.getUsername());
        };
    }

    private ConnectListener onConnected() {
        return client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Client[{}] - Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {

        return client -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Client disconnected: " + client.getSessionId());
        };
    }


}
