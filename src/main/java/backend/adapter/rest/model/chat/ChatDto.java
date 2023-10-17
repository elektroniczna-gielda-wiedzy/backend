package backend.adapter.rest.model.chat;

import backend.adapter.rest.model.user.UserDto;
import backend.chat.model.Chat;
import backend.chat.model.Message;
import backend.user.model.User;
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
        User otherUser = chat.getOppositeUser(userId);
        Timestamp lastRead = chat.getLastReadForUser(userId);

        List<Message> allMessages = Optional.ofNullable(chat.getMessages()).orElseGet(Collections::emptyList);
        List<MessageDto> allMessageDtos = allMessages.stream()
                .map(MessageDto::buildFromObject)
                .toList();

        // Find all unread messages sent by the other user
        List<Message> unreadMessages = allMessages.stream()
                .filter(msg -> !msg.getSenderUser().getId().equals(userId) && msg.getDateSent().after(lastRead))
                .toList();

        // If there are no unread messages, then the chat is considered read
        boolean isRead = unreadMessages.isEmpty();

        // Get the last message (if there is one)
        Optional<MessageDto> lastMessageDto = allMessageDtos.stream()
                .max(Comparator.comparing(MessageDto::getDateSent));

        return ChatDto.builder()
                .chatId(chat.getId())
                .otherUserId(otherUser.getId())
                .otherUser(UserDto.buildFromModel(otherUser, null, false))
                .messages(allMessageDtos)
                .lastMessage(lastMessageDto.orElse(null))
                .isRead(isRead)
                .build();
    }
}
