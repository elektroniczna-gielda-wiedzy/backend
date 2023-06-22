package backend.services;

import backend.model.AppUserDetails;
import backend.model.StandardResponse;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void handleNewMessage(
            AppUserDetails userDetails,
            String chatId,
            String chatMessage
    ) {
        Integer userId = userDetails.getId();

        System.out.println("New chat message from user with id: " + userId + "\nContent: " + chatMessage);

        // TODO: persist message and get recipient id
        String recipientId = "adamkowalski@student.agh.edu.pl";

        this.simpMessagingTemplate.convertAndSendToUser(recipientId, "/queue/notification", "You have a new message!");
        this.simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, chatMessage);
    }


    public ResponseEntity<StandardResponse> getChatList(
            AppUserDetails userDetails
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> getChat(
            AppUserDetails userDetails,
            Integer chatId
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> createChat(
            AppUserDetails userDetails,
            Integer userId
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> getUnreadChatList(
            AppUserDetails userDetails
            ) {
        StandardResponse response = StandardResponse.builder().success(true)
                .messages(List.of())
                .result(
                        List.of(new Random().nextInt(100)) // number of unread chats
                ).build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<StandardResponse> readChat(
            AppUserDetails userDetails,
            Integer chatId
            ) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
