package backend.adapter.rest.controller;

import backend.adapter.rest.model.chat.ChatDto;
import backend.adapter.rest.model.chat.MessageDto;
import backend.chat.model.Message;
import backend.user.model.AppUserDetails;
import backend.chat.model.Chat;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.chat.service.ChatService;
import backend.common.service.GenericServiceException;
import backend.user.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String MESSAGE_QUEUE = "/queue/message";
    private final String NOTIFICATION_QUEUE = "/queue/notification";

    public ChatController(ChatService chatService, SimpMessagingTemplate simpMessagingTemplate) {
        this.chatService = chatService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/{chatId}")
    public void newMessage(@AuthenticationPrincipal Authentication authentication,
                           @DestinationVariable Integer chatId,
                           @Payload String chatMessage) {

        Integer userId = ((AppUserDetails) authentication.getPrincipal()).getId();
        String chatContent;
        try {
            MessageDto messageDto = new ObjectMapper().readValue(chatMessage, MessageDto.class);
            chatContent = messageDto.getContent();
        } catch (JsonProcessingException e) {
            this.simpMessagingTemplate.convertAndSendToUser(
                    authentication.getName(),
                    MESSAGE_QUEUE,
                    Response.builder()
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .addMessage("Error parsing message")
                            .build()
            );
            return;
        }

        Message message;
        User oppositeUser;
        try {
            message = this.chatService.createMessage(chatId, userId, chatContent);
            oppositeUser = message.getChat().getOppositeUser(userId);
        } catch (Exception exception) {
            this.simpMessagingTemplate.convertAndSendToUser(
                    authentication.getName(),
                    MESSAGE_QUEUE,
                    Response.builder()
                            .httpStatusCode(HttpStatus.BAD_REQUEST)
                            .addMessage(exception.getMessage())
                            .build()
            );
            return;
        }

        ResponseEntity<StandardBody> response = Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(MessageDto.buildFromModel(message)))
                .build();

                
        this.simpMessagingTemplate.convertAndSendToUser(
                authentication.getName(),
                MESSAGE_QUEUE,
                response
        );

        this.simpMessagingTemplate.convertAndSendToUser(
                oppositeUser.getEmail(),
                MESSAGE_QUEUE,
                response
        );

        this.simpMessagingTemplate.convertAndSendToUser(
                oppositeUser.getEmail(),
                NOTIFICATION_QUEUE,
                chatId
        );
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
