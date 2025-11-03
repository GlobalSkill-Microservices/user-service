package com.globalskills.user_service.ChatBox.Dto;

import com.globalskills.user_service.Account.Dto.AccountDto;
import com.globalskills.user_service.ChatBox.Enum.ConversationType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationResponse {
    String id;

    ConversationType type;

    List<AccountDto> participants;

    String groupName;
    String groupAvatar;
    AccountDto admin;

    String lastMessageContent;

    Instant lastMessageTimestamp;

    List<Long> hiddenFor;
}
