package backend.adapter.rest.controller;

import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.user.model.AppUserDetails;
import backend.user.model.ExtendedUserDto;
import backend.user.model.User;
import backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<StandardBody> getUserInfo(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                    @PathVariable("user_id") Integer userId) {

        ExtendedUserDto userDto;
        try {
            userDto = this.userService.getUserInfo(userId, appUserDetails.getId());
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }
        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(userDto))
                .build();
    }


}
