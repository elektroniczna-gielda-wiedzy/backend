package backend.controllers;

import backend.model.AppUserDetails;
import backend.model.dto.ChatDto;
import backend.rest.common.StandardBody;
import backend.services.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/{chatId}")
    public void newMessage(
            @AuthenticationPrincipal Authentication authentication,
            @DestinationVariable String chatId,
            @Payload String chatMessage) {
        this.chatService.handleNewMessage((AppUserDetails) authentication.getPrincipal(), chatId, chatMessage);
    }

    @GetMapping()
    public ResponseEntity<StandardBody> getChatList(
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return this.chatService.getChatList(userDetails);
    }

    @PostMapping()
    public ResponseEntity<StandardBody> createChat(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @RequestBody ChatDto chatDto) {
        return this.chatService.createChat(userDetails, chatDto.getOtherUserId());
    }

    @GetMapping("/{chat_id}")
    public ResponseEntity<StandardBody> getChat(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("chat_id") Integer chatId) {
        return this.chatService.getChat(userDetails, chatId);
    }

    @GetMapping("/unread")
    public ResponseEntity<StandardBody> getUnreadChatList(
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return this.chatService.getUnreadChatList(userDetails);
    }

    @PutMapping("/{chat_id}/read")
    public ResponseEntity<StandardBody> readChat(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @PathVariable("chat_id") Integer chatId) {
        return this.chatService.readChat(userDetails, chatId);
    }
}

