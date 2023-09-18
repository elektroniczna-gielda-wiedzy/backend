package backend.rest.chat.model;

import backend.model.dao.Chat;
import backend.model.dao.Message;
import backend.model.dao.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Builder
@Getter
@Jacksonized
public class ChatDto {
    @JsonProperty("chat_id")
    private Integer chatId;

    // TODO: Remove?
    @JsonProperty("other_user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "other_user_id cannot be null")
    private Integer otherUserId;

    @JsonProperty("other_user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto otherUser;

    @JsonProperty("messages")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MessageDto> messages;

    @JsonProperty("last_message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MessageDto lastMessage;

    @JsonProperty("is_read")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRead;

    public static ChatDto buildFromModel(Chat chat, Integer userId) {
        User otherUser = chat.getUserOne().getId().equals(userId) ? chat.getUserTwo() : chat.getUserOne();
        Optional<Message> lastMessageOptional = Optional.ofNullable(chat.getMessages())
                .orElseGet(Collections::emptyList)
                .stream()
                .min(Comparator.comparing(Message::getDateSent));

        boolean isRead = true;
        if (lastMessageOptional.isPresent()) {
            Message lastMessage = lastMessageOptional.get();
            Timestamp lastRead = chat.getLastReadForUser(userId);
            if (!lastMessage.getSenderUser().getId().equals(userId) && lastMessage.getDateSent().after(lastRead)) {
                isRead = false;
            }
        }

        return ChatDto.builder()
                .chatId(chat.getId())
                .otherUserId(otherUser.getId())
                .otherUser(UserDto.buildFromObject(otherUser))
                .messages(Optional.ofNullable(chat.getMessages())
                                  .orElseGet(Collections::emptyList)
                                  .stream()
                                  .map(MessageDto::buildFromObject)
                                  .toList())
                .lastMessage(lastMessageOptional.map(MessageDto::buildFromObject).orElse(null))
                .isRead(isRead)
                .build();
    }
}
