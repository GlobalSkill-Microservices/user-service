package com.globalskills.user_service.ChatBox.Dto;

import com.globalskills.user_service.ChatBox.Document.Conversation;
import com.globalskills.user_service.ChatBox.Document.Message;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatResponse {
    Message message;
    Conversation conversation;
}
