package backend.model.dto;

import backend.model.dao.UserDao;
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
    private Date dateSent;
}
