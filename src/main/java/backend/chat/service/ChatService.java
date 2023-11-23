package backend.chat.service;

import backend.chat.model.Chat;
import backend.chat.model.Message;
import backend.user.model.User;
import backend.chat.repository.ChatRepository;
import backend.chat.repository.MessageRepository;
import backend.user.repository.UserRepository;
import backend.common.service.GenericServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static backend.chat.model.Chat.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class ChatService {


    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    public ChatService(ChatRepository chatRepository,
                       UserRepository userRepository,
                       MessageRepository messageRepository) {
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
                    return !this.messageRepository.findMessagesBySenderUserAndDateSentGreaterThan(
                            oppositeUser, lastReadDate).isEmpty();
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

        if (!chats.isEmpty()) {
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

    public Message createMessage(Integer chatId, Integer userId, String chatContent) {
        Chat chat = this.chatRepository.findById(chatId).orElseThrow(
                () -> new GenericServiceException(String.format("Chat with id = %d does not exist", chatId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        Message message = new Message();
        message.setChat(chat);
        message.setDateSent(Timestamp.from(Instant.now()));
        message.setSenderUser(user);
        message.setContent(chatContent);

        try {
            this.messageRepository.save(message);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return message;
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
