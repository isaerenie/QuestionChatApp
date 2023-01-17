package net.saga.questionchat.repo;

import net.saga.questionchat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRoom(String room);
}