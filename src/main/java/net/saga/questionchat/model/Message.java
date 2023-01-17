package net.saga.questionchat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.saga.questionchat.util.MessageType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message extends Base {
    private String message;
    private String username;
    private String room;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
}
