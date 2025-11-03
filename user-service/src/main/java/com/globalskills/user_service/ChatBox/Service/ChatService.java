package com.globalskills.user_service.ChatBox.Service;

import com.globalskills.user_service.Account.Dto.AccountDto;
import com.globalskills.user_service.Account.Entity.Account;
import com.globalskills.user_service.Account.Service.AccountQueryService;
import com.globalskills.user_service.ChatBox.Document.Conversation;
import com.globalskills.user_service.ChatBox.Document.Message;
import com.globalskills.user_service.ChatBox.Dto.ChatResponse;
import com.globalskills.user_service.ChatBox.Dto.ConversationGroupRequest;
import com.globalskills.user_service.ChatBox.Dto.ConversationResponse;
import com.globalskills.user_service.ChatBox.Dto.SendMessageRequest;
import com.globalskills.user_service.ChatBox.Enum.ConversationType;
import com.globalskills.user_service.ChatBox.Exception.ConversationException;
import com.globalskills.user_service.ChatBox.Repository.ConversationMGRepo;
import com.globalskills.user_service.ChatBox.Repository.MessageMGRepo;
import com.globalskills.user_service.Common.Dto.PageResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AccountQueryService accountQueryService;

    @Autowired
    MessageMGRepo messageMGRepo;

    @Autowired
    ConversationMGRepo conversationMGRepo;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public ChatResponse sendMessage(Long senderId, SendMessageRequest request) {

        Account account = accountQueryService.findAccountById(senderId);
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        Conversation conversation;

        if (request.getConversationId() != null) {
            conversation = conversationMGRepo.findById(request.getConversationId())
                    .orElseThrow(() -> new ConversationException("Cant found conversation", HttpStatus.NOT_FOUND));

        } else if (request.getRecipientId() != null) {
            conversation = conversationMGRepo.findConversationByParticipantsAndType(
                            ConversationType.ONE_TO_ONE,
                            senderId,
                            request.getRecipientId()
                    )
                    .orElseGet(() -> create(senderId, request.getRecipientId(), ConversationType.ONE_TO_ONE));
        } else {
            throw new ConversationException("Missing field", HttpStatus.BAD_REQUEST);
        }

        boolean isParticipant = conversation.getParticipants().stream()
                .anyMatch(p -> p.getId().equals(senderId));

        if (!isParticipant) {
            throw new ConversationException("you're not participant in this conversation", HttpStatus.FORBIDDEN);
        }
        Message message = new Message();
        message.setConversationId(conversation.getId());
        message.setSender(accountDto);
        message.setContent(request.getContent());
        message.setTimestamp(Instant.now());
        messageMGRepo.save(message);

        updateConversationLastMessage(conversation, message);

        String topicDestination = "/topic/conversation/" + conversation.getId();
        simpMessagingTemplate.convertAndSend(topicDestination, message);

        String privateDestination = "/queue/private";

        final Long finalSenderId = senderId;

        conversation.getParticipants().stream()
                .filter(participant -> !participant.getId().equals(finalSenderId))
                .forEach(recipient -> {


                    String recipientIdString = String.valueOf(recipient.getId());

                    simpMessagingTemplate.convertAndSendToUser(
                            recipientIdString,
                            privateDestination,
                            message
                    );
                });

        return new ChatResponse(message, conversation);
    }


    public Conversation createConversationTypeGroup(Long adminId, ConversationGroupRequest request){

        Account account = accountQueryService.findAccountById(adminId);
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        List<Account> accountList = accountQueryService.findAllAccountById(request.getIds());
        List<AccountDto> accountDtos = accountList
                .stream()
                .map(acc -> modelMapper.map(acc, AccountDto.class))
                .toList();


        Conversation conversation = new Conversation();
        conversation.setType(ConversationType.GROUP);
        conversation.setAdmin(accountDto);
        conversation.setParticipants(accountDtos);
        conversation.setGroupName(request.getGroupName());
        conversation.setGroupAvatar(request.getGroupAvatar());

        conversationMGRepo.save(conversation);

        return conversation;
    }


    public void deleteConversation(Long accountId, String conversationId) {
        Conversation conversation = conversationMGRepo.findById(conversationId)
                .orElseThrow(() -> new ConversationException("Cant found conversation", HttpStatus.NOT_FOUND));

        if (conversation.getType() == ConversationType.GROUP) {
            conversationMGRepo.pullParticipantById(conversationId, accountId);

        } else {
            conversationMGRepo.addHiddenForUser(conversationId, accountId);
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<ConversationResponse> getConversations(
            Long accountId,
            int page,
            int size
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "lastMessageTimestamp");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Conversation> conversationPage = conversationMGRepo.findByParticipants_IdAndHiddenForNot(
                accountId,
                accountId,
                pageRequest
        );

        if (conversationPage.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(), page, size, 0, 0, true);
        }

        List<ConversationResponse> responses = conversationPage
                .stream()
                .map(this::mapToConversationResponse)
                .toList();

        return new PageResponse<>(
                responses,
                page,
                size,
                conversationPage.getTotalElements(),
                conversationPage.getTotalPages(),
                conversationPage.isLast()
        );
    }

    private ConversationResponse mapToConversationResponse(Conversation conversation) {
        ConversationResponse response = modelMapper.map(conversation, ConversationResponse.class);

        if (conversation.getLastMessageId() != null) {
            Optional<Message> lastMessageOpt = messageMGRepo.findById(conversation.getLastMessageId());

            if (lastMessageOpt.isPresent()) {
                response.setLastMessageContent(lastMessageOpt.get().getContent());
            } else {
                response.setLastMessageContent("Tin nhắn đã bị thu hồi/xóa");
            }
        } else {
            response.setLastMessageContent("Chưa có tin nhắn.");
        }
        return response;
    }


    @Transactional(readOnly = true)
    public PageResponse<Message> getMessagesByConversation(
            String conversationId,
            int page,
            int size
    ) {
        Sort.Direction direction = Sort.Direction.DESC;
        String sortBy = "timestamp";

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));


        Page<Message> messagePage = messageMGRepo.findByConversationId(conversationId, pageRequest);

        if (messagePage.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(), page, size, 0, 0, true);
        }

        List<Message> responses = messagePage
                .stream()
                .map(message -> modelMapper.map(message, Message.class))
                .toList();

        return new PageResponse<>(
                responses,
                page,
                size,
                messagePage.getTotalElements(),
                messagePage.getTotalPages(),
                messagePage.isLast()
        );
    }


    private Conversation create(Long senderId, Long recipientId,ConversationType type){
        Conversation conversation = new Conversation();
        List<Account> accountList = accountQueryService.findAllAccountById(List.of(senderId,recipientId));
        List<AccountDto> accountDtos = accountList
                .stream()
                .map(account -> modelMapper.map(account, AccountDto.class))
                .toList();
        conversation.setParticipants(accountDtos);
        conversation.setType(type);
        conversationMGRepo.save(conversation);
        return conversation;
    }

    private void updateConversationLastMessage(Conversation conversation, Message lastMessage){
        conversation.setLastMessageId(lastMessage.getId());
        conversation.setLastMessageTimestamp(lastMessage.getTimestamp());
        if (conversation.getHiddenFor() != null && !conversation.getHiddenFor().isEmpty()) {
            conversation.setHiddenFor(new ArrayList<>());
        }
        conversationMGRepo.save(conversation);
    }

}
