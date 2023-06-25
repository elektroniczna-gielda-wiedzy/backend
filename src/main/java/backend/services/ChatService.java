package backend.services;

import backend.model.AppUserDetails;
import backend.model.StandardResponse;
import backend.model.dao.ChatDao;
import backend.model.dao.EntryDao;
import backend.model.dao.MessageDao;
import backend.model.dao.UserDao;
import backend.model.dto.ChatDto;
import backend.model.dto.MessageDto;
import backend.repositories.ChatRepository;
import backend.repositories.MessageRepository;
import backend.repositories.UserRepository;
import backend.util.ExchangeAppUtils;
import backend.util.ResponseUtil;
import backend.util.UserDaoDtoConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final SessionFactory sessionFactory;

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatRepository chatRepository, UserRepository userRepository,
                       MessageRepository messageRepository, SessionFactory sessionFactory) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.sessionFactory = sessionFactory;
    }

    public void handleNewMessage(
            AppUserDetails userDetails,
            String chatId,
            String chatMessage
    ) {
        Integer userId = userDetails.getId();

        System.out.println("New chat message from user with id: " + userId + "\nContent: " + chatMessage);

        ChatDao chat = chatRepository.findChatDaoByChatId(Integer.parseInt(chatId));
        UserDao oppositeUser = ExchangeAppUtils.getOppositeUser(userDetails.getId(), chat);
        MessageDao messageDao = new MessageDao();
        messageDao.setChatDao(chat);

        try {
            MessageDto messageDto = new ObjectMapper().readValue(chatMessage, MessageDto.class);
            messageDao.setContent(messageDto.getContent());
        } catch(JsonProcessingException e) {
            System.out.println(e.getMessage());
            return;
        }


        messageDao.setDateSent(Timestamp.from(Instant.now()));
        messageDao.setSenderUserDao(ExchangeAppUtils.getCurrentUserDao(userId, chat));
        messageRepository.save(messageDao);
        MessageDto messageDto = MessageDto.builder()
                .messageId(messageDao.getMessageId())
                .sender(UserDaoDtoConverter.convertToDto(messageDao.getSenderUserDao()))
                .content(messageDao.getContent())
                .dateSent(new Date(messageDao.getDateSent().getTime()))
                .chatId(messageDao.getChatDao().getChatId())
                .build();
        try {
            this.simpMessagingTemplate.convertAndSendToUser(oppositeUser.getEmail(), "/queue/notification", new ObjectMapper().writeValueAsString(ChatDto.builder().chatId(Integer.parseInt(chatId)).build()));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        this.simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, messageDto);
    }

    //tested
    public ResponseEntity<StandardResponse> getChatList(
            AppUserDetails userDetails
            ) {
        UserDao userDao = userRepository.findUserDaoByUserId(userDetails.getId());
        List<ChatDao> chatDaos = chatRepository.findChatDaosByUserOneDaoOrUserTwoDao(userDao, userDao);
        Session session = sessionFactory.openSession();
        List<ChatDto> chatDtos;
        try {
            chatDtos = chatDaos.stream().map((chatDao) -> {
                CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
                CriteriaQuery<MessageDao> criteriaQuery = criteriaBuilder.createQuery(MessageDao.class);
                Root<MessageDao> root = criteriaQuery.from(MessageDao.class);
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateSent")));
                Predicate belongsToChat = criteriaBuilder.equal(root.get("chatDao"), chatDao.getChatId());
                criteriaQuery.where(belongsToChat);
                List<MessageDao> messageDaos = session.createQuery(criteriaQuery).setMaxResults(1).getResultList();
                MessageDao lastMessage;
                if(messageDaos.size() > 0) {
                    lastMessage = messageDaos.get(0);
                    return ChatDto.builder()
                            .chatId(chatDao.getChatId())
                            .otherUser(UserDaoDtoConverter.convertToDto(ExchangeAppUtils.getOppositeUser(userDao.getUserId(), chatDao)))
                            .lastMessage(MessageDto.builder()
                                    .messageId(lastMessage.getMessageId())
                                    .sender(UserDaoDtoConverter.convertToDto(lastMessage.getSenderUserDao()))
                                    .content(lastMessage.getContent())
                                    .dateSent(new Date(lastMessage.getDateSent().getTime()))
                                    .chatId(chatDao.getChatId())
                                    .build())
                            .build();
                } else {
                    return null;
                }

            }).filter((Objects::nonNull)).sorted((chatDto1, chatDto2) -> {
                if (chatDto1.getLastMessage().getDateSent().before(chatDto2.getLastMessage().getDateSent())) {
                    return 1;
                } else if (chatDto1.getLastMessage().getDateSent().after(chatDto2.getLastMessage().getDateSent())){
                    return -1;
                } else {
                    return 0;
                }
            }).toList();

        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(StandardResponse.builder()
                    .success(false)
                    .messages(List.of(e.getMessage()))
                    .result(List.of())
                    .build());

        } finally {
            session.close();
        }
        return ResponseEntity.ok(StandardResponse.builder()
                .success(true)
                .messages(List.of())
                .result(chatDtos)
                .build());
    }

    //tested
    public ResponseEntity<StandardResponse> getChat(
            AppUserDetails userDetails,
            Integer chatId
            ) {
        try {
            ChatDao chatDao = chatRepository.findChatDaoByChatId(chatId);
            if(!userDetails.getId().equals(chatDao.getUserTwoDao().getUserId()) && !userDetails.getId().equals(chatDao.getUserOneDao().getUserId())) {
                throw new AccessDeniedException("You do not have access to this chat");
            }
            List<MessageDao> messages = messageRepository.findMessageDaosByChatDao(chatDao);
            List<MessageDto> messagesReturned = messages.stream().map((message) -> MessageDto.builder()
                .messageId(message.getMessageId())
                .sender(UserDaoDtoConverter.convertToDto(message.getSenderUserDao()))
                .chatId(chatDao.getChatId())
                .content(message.getContent())
                .dateSent(message.getDateSent()).build()).toList();
            return ResponseEntity.ok(
                StandardResponse.builder()
                    .success(true)
                    .messages(List.of())
                    .result(List.of(ChatDto.builder()
                        .chatId(chatDao.getChatId())
                        .otherUserId(ExchangeAppUtils.getOppositeUser(userDetails.getId(), chatDao).getUserId())
                        .messageDtoList(messagesReturned)
                        .build()))
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.builder()
                            .success(false)
                            .messages(List.of(e.getMessage()))
                            .result(List.of())
                            .build());
        }

    }

    public ResponseEntity<StandardResponse> createChat(
            AppUserDetails userDetails,
            Integer userId
            ) {
        UserDao userOneDao = userRepository.findUserDaoByUserId(userDetails.getId());
        UserDao userTwoDao = userRepository.findUserDaoByUserId(userId);
        List<ChatDao> usersChats = chatRepository.findChatDaosByUserOneDaoInAndUserTwoDaoIn(List.of(userOneDao, userTwoDao),
                List.of(userOneDao, userTwoDao));
        if(usersChats.size() > 0) {
            return getChat(userDetails, usersChats.get(0).getChatId());
        }
        ChatDao chatDao = new ChatDao();
        chatDao.setUserOneDao(userOneDao);
        chatDao.setUserTwoDao(userTwoDao);
        chatDao.setUserOneLastRead(Timestamp.from(Instant.now()));
        //temporary solution
        chatDao.setUserTwoLastRead(Timestamp.from(Instant.now().minusSeconds(60)));
        chatRepository.save(chatDao);
        try {
            this.simpMessagingTemplate.convertAndSendToUser(userTwoDao.getEmail(), "/queue/notification", new ObjectMapper().writeValueAsString(ChatDto.builder().chatId(chatDao.getChatId()).build()));
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        return getChat(userDetails, chatDao.getChatId());
    }

    public ResponseEntity<StandardResponse> getUnreadChatList(
            AppUserDetails userDetails
            ) {
        UserDao userDao = userRepository.findUserDaoByUserId(userDetails.getId());
        List<ChatDao> userChats = chatRepository.findChatDaosByUserOneDaoOrUserTwoDao(userDao, userDao);
        int noUnreadChats = userChats.stream().filter((userChat) -> {
            UserDao oppositeUser;
            Timestamp currentUserLastReadDate;
            if(userDetails.getId().equals(userChat.getUserOneDao().getUserId())) {
                oppositeUser = userChat.getUserTwoDao();
                currentUserLastReadDate = userChat.getUserOneLastRead();
            } else {
                oppositeUser = userChat.getUserOneDao();
                currentUserLastReadDate = userChat.getUserTwoLastRead();
            }
            List<MessageDao> unreadMessages = messageRepository.findMessageDaosBySenderUserDaoAndDateSentGreaterThan(oppositeUser, currentUserLastReadDate);
            return unreadMessages.size() > 0;
        }).toList().size();
        StandardResponse response = StandardResponse.builder().success(true)
                .messages(List.of())
                .result(
                        List.of(noUnreadChats) // number of unread chats
                ).build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<StandardResponse> readChat(
            AppUserDetails userDetails,
            Integer chatId
            ) {
        ChatDao chatDao = chatRepository.findChatDaoByChatId(chatId);
        if(userDetails.getId().equals(chatDao.getUserOneDao().getUserId())) {
            chatDao.setUserOneLastRead(Timestamp.from(Instant.now()));
        } else {
            chatDao.setUserTwoLastRead(Timestamp.from(Instant.now()));
        }
        chatRepository.save(chatDao);
        return ResponseEntity.ok(
                StandardResponse.builder()
                        .success(true)
                        .messages(List.of())
                        .result(List.of())
                        .build()
        );
    }
}
