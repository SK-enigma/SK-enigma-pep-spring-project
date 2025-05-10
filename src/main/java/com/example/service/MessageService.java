package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


  //Manages CRUD operations and validation rules for Message entities.
 

@Service
public class MessageService {
    private final MessageRepository msgRepo;
    private final AccountRepository accRepo;

    public MessageService(MessageRepository msgRepo, AccountRepository accRepo) {
        this.msgRepo = msgRepo;
        this.accRepo = accRepo;
    }

    @Transactional
    public Message create(Message m) {
        if (m.getMessageText() == null || m.getMessageText().isBlank() || m.getMessageText().length() > 255)
            throw new IllegalArgumentException("Invalid message text");
        if (!accRepo.existsById(m.getPostedBy()))
            throw new IllegalArgumentException("Unknown user");
        return msgRepo.save(m);
    }

    public List<Message> getAll() {
        return msgRepo.findAll();
    }

    public Message getOne(Integer id) {
        return msgRepo.findById(id).orElse(null);
    }

    @Transactional
    public int delete(Integer id) {
        if (!msgRepo.existsById(id)) return 0;
        msgRepo.deleteById(id);
        return 1;
    }

    @Transactional
    public int updateText(Integer id, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255)
            throw new IllegalArgumentException("Invalid message text");
        return msgRepo.findById(id).map(m -> {
            m.setMessageText(newText);
            msgRepo.save(m);
            return 1;
        }).orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    public List<Message> getByUser(Integer accountId) {
        return msgRepo.findByPostedBy(accountId);
    }
}
