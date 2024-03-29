package backend.adapter.rest.controller;

import backend.adapter.rest.Paginator;
import backend.adapter.rest.Response;
import backend.adapter.rest.ResultInfo;
import backend.adapter.rest.StandardBody;
import backend.adapter.rest.model.user.BanDto;
import backend.adapter.rest.model.user.UserDto;
import backend.common.service.GenericServiceException;
import backend.entry.model.Entry;
import backend.user.model.AppUserDetails;
import backend.user.model.User;
import backend.user.service.UserService;
import jakarta.validation.Valid;
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

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getUsers(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                 @RequestParam Map<String, String> params) {
        List<User> users;
        Integer page;
        Integer perPage;

        try {
            String query = params.get("query");
            Boolean isBanned = params.get("is_banned") != null ? Boolean.parseBoolean(params.get("is_banned")) : null;
            Boolean isEmailAuth = params.get("is_email_auth") != null ? Boolean.parseBoolean(params.get("is_email_auth")) : null;
            page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
            perPage = params.get("per_page") != null ? Integer.parseInt(params.get("per_page")) : null;

            users = this.userService.findUserByQuery(query, isBanned, isEmailAuth);
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        Paginator<User> paginator = new Paginator<>(users, page, perPage);

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(paginator.getResult().stream()
                                .map(u -> UserDto.buildFromModel(u, appUserDetails.getUser(), false))
                                .toList())
                .resultInfo(ResultInfo.buildFromPaginator(paginator))
                .build();
    }

    @PutMapping(path = "/{user_id}/ban", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> banUser(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                                @PathVariable("user_id") Integer userId,
                                                @Valid @RequestBody BanDto banDto) {
        try {
            this.userService.setUserBanned(appUserDetails.getId(), userId, banDto.getValue());
        } catch (Exception exception) {
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
