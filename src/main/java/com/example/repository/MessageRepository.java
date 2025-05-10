package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


 // Provides CRUD operations for Message entities
 // and a finder method to retrieve all messages by a given user.
 

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findByPostedBy(Integer accountId);
}
