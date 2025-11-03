package com.globalskills.user_service.ChatBox.Document;

import com.globalskills.user_service.Account.Dto.AccountDto;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "Message")
@CompoundIndex(
        name = "convo_timestamp_desc_index",
        def = "{'conversationId': 1, 'timestamp': -1}"
)
public class Message {
    @Id
    String id;
    String conversationId;
    AccountDto sender;
    String content;
    Instant timestamp;
}
