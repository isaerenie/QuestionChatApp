package net.saga.questionchat.soket;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;
import net.saga.questionchat.model.Message;
import net.saga.questionchat.service.MessageService;
import net.saga.questionchat.util.MessageType;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class SocketService {
   private final MessageService messageService;

    public SocketService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void sendSocketMessage(String room, String eventName, SocketIOClient senderClient, String message, String username) {
        Collection<SocketIOClient> clients = senderClient.getNamespace().getRoomOperations(room).getClients();
        for (SocketIOClient client : clients) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Message(message, username, room, MessageType.SERVER));
            }
        }
    }
    public void saveMessage(SocketIOClient senderClient, Message message) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.CLIENT)
                .message(message.getMessage())
                .room(message.getRoom())
                .username(message.getUsername())
                .build());
        sendSocketMessage(message.getRoom(), "get_message", senderClient, storedMessage.getMessage(), storedMessage.getUsername());
    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.SERVER)
                .message(message)
                .room(room)
                .build());
        sendSocketMessage(room, "get_message", senderClient, storedMessage.getMessage(), storedMessage.getUsername());
    }
}
