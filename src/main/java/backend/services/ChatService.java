package backend.services;

import backend.model.dao.Chat;
import backend.model.dao.Message;
import backend.model.dao.User;
import backend.rest.chat.model.ChatDto;
import backend.rest.chat.model.MessageDto;
import backend.repositories.ChatRepository;
import backend.repositories.MessageRepository;
import backend.repositories.UserRepository;
import backend.rest.chat.model.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static backend.model.dao.Chat.*;
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
        Optional<Chat> chatOptional = this.chatRepository.findById(chatId);
        if (chatOptional.isEmpty()) {
            throw new GenericServiceException(String.format("Chat with id = %d does not exist", chatId));
        }
        Chat chat = chatOptional.get();

        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userId));
        }

        if (!chat.getUserOne().getId().equals(userId) && !chat.getUserTwo().getId().equals(userId)) {
            throw new GenericServiceException("You do not have access to this chat");
        }

        return chat;
    }

    public List<Chat> getChats(Integer userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userId));
        }

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
        Optional<User> userOneOptional = this.userRepository.findById(userOneId);
        if (userOneOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userOneId));
        }
        User userOne = userOneOptional.get();

        Optional<User> userTwoOptional = this.userRepository.findById(userTwoId);
        if (userTwoOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userTwoId));
        }
        User userTwo = userTwoOptional.get();

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
                .sender(UserDto.buildFromObject(message.getSenderUser()))
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
