package backend.rest.chat;

import backend.model.AppUserDetails;
import backend.model.dao.Chat;
import backend.rest.chat.model.ChatDto;
import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.services.ChatService;
import backend.services.GenericServiceException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/{chatId}")
    public void newMessage(@AuthenticationPrincipal Authentication authentication,
                           @DestinationVariable Integer chatId,
                           @Payload String chatMessage) {
        this.chatService.createMessage(chatId, ((AppUserDetails) authentication.getPrincipal()).getId(), chatMessage);
    }

    @GetMapping(path = "/{chat_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getChat(@AuthenticationPrincipal AppUserDetails userDetails,
                                                @PathVariable("chat_id") Integer chatId) {
        Chat chat;

        try {
            chat = this.chatService.getChat(chatId, userDetails.getId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(ChatDto.buildFromModel(chat, userDetails.getId())))
                .build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getChats(@AuthenticationPrincipal AppUserDetails userDetails) {
        List<Chat> chats;

        try {
            chats = this.chatService.getChats(userDetails.getId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(chats.stream().map(c -> ChatDto.buildFromModel(c, userDetails.getId())).toList())
                .build();
    }

    // TODO: Remove?
    @GetMapping(path = "/unread", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getUnreadChats(@AuthenticationPrincipal AppUserDetails userDetails) {
        int noUnreadChats;

        try {
            noUnreadChats = this.chatService.getUnreadChats(userDetails.getId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(noUnreadChats))
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createChat(@AuthenticationPrincipal AppUserDetails userDetails,
                                                   @Valid @RequestBody ChatDto chatDto) {
        Chat chat;

        try {
            chat = this.chatService.createChat(userDetails.getId(), chatDto.getOtherUserId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(ChatDto.buildFromModel(chat, userDetails.getId())))
                .build();
    }

    @PutMapping(path = "/{chat_id}/read", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> readChat(@AuthenticationPrincipal AppUserDetails userDetails,
                                                 @PathVariable("chat_id") Integer chatId) {
        try {
            this.chatService.readChat(chatId, userDetails.getId());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }
}
