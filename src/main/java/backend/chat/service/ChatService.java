package backend.chat.service;

import backend.chat.model.Chat;
import backend.chat.model.Message;
import backend.user.model.User;
import backend.adapter.rest.model.chat.ChatDto;
import backend.adapter.rest.model.chat.MessageDto;
import backend.chat.repository.ChatRepository;
import backend.chat.repository.MessageRepository;
import backend.user.repository.UserRepository;
import backend.adapter.rest.model.common.UserDto;
import backend.common.service.GenericServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static backend.chat.model.Chat.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    public ChatService(SimpMessagingTemplate simpMessagingTemplate,
                       ChatRepository chatRepository,
                       UserRepository userRepository,
                       MessageRepository messageRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public Chat getChat(Integer chatId, Integer userId) {
        Chat chat = this.chatRepository.findById(chatId).orElseThrow(
                () -> new GenericServiceException(String.format("Chat with id = %d does not exist", chatId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!chat.getUserOne().getId().equals(userId) && !chat.getUserTwo().getId().equals(userId)) {
            throw new GenericServiceException("You do not have access to this chat");
        }

        return chat;
    }

    public List<Chat> getChats(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        return this.chatRepository.findAll(where(
                (userOneIs(userId).or(userTwoIs(userId)))
        ), Sort.by("messages.dateSent").descending());
    }

    // TODO: Remove?
    public int getUnreadChats(Integer userId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        List<Chat> chats = this.chatRepository.findAll(where(
                (userOneIs(userId).or(userTwoIs(userId)))));

        return chats.stream()
                .filter((chat) -> {
                    User oppositeUser = chat.getOppositeUser(userId);
                    Timestamp lastReadDate = chat.getLastReadForUser(userId);
                    return this.messageRepository.findMessagesBySenderUserAndDateSentGreaterThan(
                            oppositeUser, lastReadDate).size() > 0;
                })
                .toList()
                .size();
    }

    public Chat createChat(Integer userOneId, Integer userTwoId) {
        User userOne = this.userRepository.findById(userOneId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userOneId)));

        User userTwo = this.userRepository.findById(userTwoId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userTwoId)));

        List <Chat> chats = this.chatRepository.findAll(where(
            (userOneIs(userOneId).and(userTwoIs(userTwoId)))
            .or(userOneIs(userTwoId).and(userTwoIs(userOneId)))
        ));

        if (chats.size() > 0) {
            return chats.get(0);
        }

        Chat chat = new Chat();
        chat.setUserOne(userOne);
        chat.setUserTwo(userTwo);
        chat.setUserOneLastRead(Timestamp.from(Instant.now()));
        // TODO: temporary solution
        chat.setUserTwoLastRead(Timestamp.from(Instant.now().minusSeconds(60)));
        this.chatRepository.save(chat);

        return chat;
    }

    public void createMessage(Integer chatId, Integer userId, String chatMessage) {
        System.out.println("New chat message from user with id: " + userId + "\nContent: " + chatMessage);

        Chat chat = this.chatRepository.findById(chatId).orElseThrow(
                () -> new GenericServiceException(String.format("Chat with id = %d does not exist", chatId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        User oppositeUser = chat.getOppositeUser(user.getId());

        Message message = new Message();
        message.setChat(chat);
        message.setDateSent(Timestamp.from(Instant.now()));
        message.setSenderUser(user);

        try {
            MessageDto messageDto = new ObjectMapper().readValue(chatMessage, MessageDto.class);
            message.setContent(messageDto.getContent());
        } catch (JsonProcessingException e) {
            throw new GenericServiceException("Invalid message format");
        }

        this.messageRepository.save(message);

        MessageDto messageDto = MessageDto.builder()
                .messageId(message.getId())
                .sender(UserDto.buildFromModel(message.getSenderUser()))
                .content(message.getContent())
                .dateSent(new Date(message.getDateSent().getTime()))
                .chatId(message.getChat().getId())
                .build();

        ChatDto chatDto = ChatDto.builder()
                .chatId(chatId)
                .build();

        try {
            this.simpMessagingTemplate.convertAndSendToUser(oppositeUser.getEmail(),
                                                            "/queue/notification",
                                                            new ObjectMapper().writeValueAsString(chatDto));
        } catch (JsonProcessingException e) {
            throw new GenericServiceException("convertAndSendToUser error: " + e.getMessage());
        }

        this.simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, messageDto);
    }

    public void readChat(Integer chatId, Integer userId) {
        Chat chat = this.chatRepository.findById(chatId).orElseThrow(
                () -> new GenericServiceException(String.format("Chat with id = %d does not exist", chatId)));

        if (userId.equals(chat.getUserOne().getId())) {
            chat.setUserOneLastRead(Timestamp.from(Instant.now()));
        } else {
            chat.setUserTwoLastRead(Timestamp.from(Instant.now()));
        }
        this.chatRepository.save(chat);
    }
}
