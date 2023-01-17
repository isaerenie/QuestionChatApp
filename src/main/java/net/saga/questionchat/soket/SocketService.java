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

    public void sendSocketMessage(SocketIOClient senderClient, Message message, String room) {
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent("get_message",
                        message);
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
        sendSocketMessage(senderClient, storedMessage, message.getRoom());
    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
        Message storedMessage = messageService.saveMessage(Message.builder()
                .messageType(MessageType.SERVER)
                .message(message)
                .room(room)
                .build());
        sendSocketMessage(senderClient, storedMessage, room);
    }
}
