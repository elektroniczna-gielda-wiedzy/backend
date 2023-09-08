package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Getter
@Jacksonized
public class ChatDto {
    @JsonProperty("chat_id")
    private Integer chatId;

    @JsonProperty("other_user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto otherUser;

    @JsonProperty("other_user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer otherUserId;

    @JsonProperty("messages")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MessageDto> messageDtoList;

    @JsonProperty("last_message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MessageDto lastMessage;

    @JsonProperty("is_read")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRead;
}
