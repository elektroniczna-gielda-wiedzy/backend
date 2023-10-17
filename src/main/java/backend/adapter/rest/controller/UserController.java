package backend.adapter.rest.controller;

import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.adapter.rest.model.user.UserDto;
import backend.common.service.GenericServiceException;
import backend.user.model.AppUserDetails;
import backend.user.model.User;
import backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{user_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getUser(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                @PathVariable("user_id") Integer userId) {
        User user;

        try {
            user = this.userService.getUser(userId);
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }
        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(UserDto.buildFromModel(user, appUserDetails.getUser(), true)))
                .build();
    }

    @GetMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getUsers(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                 @RequestParam Map<String, String> params) {
        List<User> users;

        try {
            users = this.userService.findUserByQuery(params.get("q"));
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }
        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(users.stream()
                                .map(u -> UserDto.buildFromModel(u, appUserDetails.getUser(), false))
                                .toList())
                .build();
    }
}
