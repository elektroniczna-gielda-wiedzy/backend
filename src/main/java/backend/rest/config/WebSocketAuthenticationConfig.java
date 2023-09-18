package backend.rest.config;

import backend.model.AppUserDetails;
import backend.model.User;
import backend.repository.UserRepository;
import backend.rest.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    public WebSocketAuthenticationConfig(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String jwtToken = accessor.getFirstNativeHeader("Authorization");

                    if (jwtToken == null || !jwtService.isValid(jwtToken)) {
                        throw new AccessDeniedException("Unauthorized");
                    }

                    Claims userClaims = jwtService.getClaims(jwtToken);
                    Integer userId = ((Double) userClaims.get("subject")).intValue();
                    String role = (String) userClaims.get("role");
                    List<GrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(role));
                    User user = userRepository.findById(userId).get();
                    AppUserDetails userDetails = new AppUserDetails(user);
                    Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,
                                                                                  null,
                                                                                  grantedAuthorityList);
                    accessor.setUser(auth);
                }

                return message;
            }
        });
    }
}

