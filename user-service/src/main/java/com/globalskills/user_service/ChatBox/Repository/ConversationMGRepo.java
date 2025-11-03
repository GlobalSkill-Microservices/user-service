package com.globalskills.user_service.ChatBox.Repository;

import com.globalskills.user_service.ChatBox.Document.Conversation;
import com.globalskills.user_service.ChatBox.Enum.ConversationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface ConversationMGRepo extends MongoRepository<Conversation,String> {
    Conversation findConversationById(String conversationId);

    @Query("{ 'type': ?0, 'participants.id': { $all: [?1, ?2] } }")
    Optional<Conversation> findConversationByParticipantsAndType(ConversationType conversationType, Long senderId, Long recipientId);

    @Query(value = "{ '_id': ?0 }" )
    @Update("{ $pull: { 'participants': { 'id': ?1 } } }")
    void pullParticipantById(String conversationId, Long userIdToPull);

    @Query("{ '_id': ?0 }")
    @Update("{ $addToSet: { 'hiddenFor': ?1 } }")
    void addHiddenForUser(String conversationId, Long accountId);

    Page<Conversation> findByParticipants_IdAndHiddenForNot(Long accountId, Long accountIdToHide, PageRequest pageRequest);
}
