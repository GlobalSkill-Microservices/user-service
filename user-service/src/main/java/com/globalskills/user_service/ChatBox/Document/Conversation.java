package com.globalskills.user_service.ChatBox.Document;

import com.globalskills.user_service.Account.Dto.AccountDto;
import com.globalskills.user_service.ChatBox.Enum.ConversationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "Conversation")
public class Conversation {
    @Id
    String id;
    ConversationType type;
    @Indexed(name = "participants_index")
    List<AccountDto> participants;

    String groupName;
    String groupAvatar;
    AccountDto admin;

    String lastMessageId;
    @Indexed(name = "last_message_timestamp_index")
    Instant lastMessageTimestamp;

    List<Long> hiddenFor = new ArrayList<>();
}
