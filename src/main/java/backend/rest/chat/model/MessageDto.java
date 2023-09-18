package backend.rest.chat.model;

import backend.model.dao.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Builder
@Getter
@Jacksonized
public class MessageDto {
    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("chat_id")
    private Integer chatId;

    @JsonProperty("sender")
    private UserDto sender;

    @JsonProperty("content")
    private String content;

    @JsonProperty("date_sent")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date dateSent;

    public static MessageDto buildFromObject(Message message) {
        return MessageDto.builder()
                .messageId(message.getId())
                .chatId(message.getChat().getId())
                .sender(UserDto.buildFromObject(message.getSenderUser()))
                .content(message.getContent())
                .dateSent(message.getDateSent())
                .build();
    }
}
