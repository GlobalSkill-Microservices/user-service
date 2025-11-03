package com.globalskills.user_service.ChatBox.Repository;

import com.globalskills.user_service.ChatBox.Document.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageMGRepo extends MongoRepository<Message,String> {
    Page<Message> findByConversationId(String conversationId, PageRequest pageRequest);
}
