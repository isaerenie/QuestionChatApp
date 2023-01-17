package net.saga.questionchat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.saga.questionchat.model.Message;
import net.saga.questionchat.repo.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;


    public List<Message> getMessages(String room) {
        return messageRepository.findAllByRoom(room);
    }

    public Message saveMessage(Message message) {
        System.out.println("getMessages");
        return messageRepository.save(message);
    }

}