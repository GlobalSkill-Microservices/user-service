package com.globalskills.user_service.ChatBox.Controller;

import com.globalskills.user_service.ChatBox.Document.Conversation;
import com.globalskills.user_service.ChatBox.Document.Message;
import com.globalskills.user_service.ChatBox.Dto.ChatResponse;
import com.globalskills.user_service.ChatBox.Dto.ConversationGroupRequest;
import com.globalskills.user_service.ChatBox.Dto.ConversationResponse;
import com.globalskills.user_service.ChatBox.Dto.SendMessageRequest;
import com.globalskills.user_service.ChatBox.Service.ChatService;
import com.globalskills.user_service.Common.Dto.BaseResponseAPI;
import com.globalskills.user_service.Common.Dto.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@SecurityRequirement(name = "api")
public class ChatBoxController {

    @Autowired
    ChatService chatService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@Parameter(hidden = true)
                                         @RequestHeader(value = "X-User-ID",required = false)
                                         Long senderId,
                                         @RequestBody SendMessageRequest request){
        String receiver = String.valueOf(request.getRecipientId());
        ChatResponse response = chatService.sendMessage(senderId, request);
        BaseResponseAPI<ChatResponse> responseAPI = new BaseResponseAPI<>(true,"Send message successfully",response,null);
        simpMessagingTemplate.convertAndSendToUser(receiver,"/queue/private",request.getContent());
        return ResponseEntity.ok(responseAPI);
    }

    @PostMapping("/conversation/group")
    public ResponseEntity<?> createConversationTypeGroup(@Parameter(hidden = true)
                                                         @RequestHeader(value = "X-User-ID",required = false)
                                                         Long adminId,
                                                         @RequestBody ConversationGroupRequest request){
        Conversation response = chatService.createConversationTypeGroup(adminId, request);
        BaseResponseAPI<Conversation> responseAPI = new BaseResponseAPI<>(true,"Create group message successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @DeleteMapping("/conversation/{conversationId}")
    public ResponseEntity<?> deleteConversation(@Parameter(hidden = true)
                                                @RequestHeader(value = "X-User-ID",required = false)
                                                Long accountId,
                                                @PathVariable String conversationId){
        chatService.deleteConversation(accountId, conversationId);
        BaseResponseAPI<?> responseAPI = new BaseResponseAPI<>(true,"Delete conversation successfully",null,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/conversations")
    public ResponseEntity<?> getConversations(@Parameter(hidden = true)
                                              @RequestHeader(value = "X-User-ID",required = false)
                                              Long accountId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size){
        PageResponse<ConversationResponse> response = chatService.getConversations(accountId, page, size);
        BaseResponseAPI<PageResponse<ConversationResponse>> responseAPI = new BaseResponseAPI<>(true,"Get conversation from account: "+accountId+" successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }

    @GetMapping("/messages/conversation/{conversationId}")
    public ResponseEntity<?> getMessagesByConversation(@PathVariable String conversationId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){
        PageResponse<Message> response = chatService.getMessagesByConversation(conversationId, page, size);
        BaseResponseAPI<PageResponse<Message>> responseAPI = new BaseResponseAPI<>(true,"Get messages from conversation: "+conversationId+" successfully",response,null);
        return ResponseEntity.ok(responseAPI);
    }


}
