package backend.util;

import backend.model.dao.User;
import backend.model.dto.UserDto;

public class UserDaoDtoConverter {
    public static UserDto convertToDto(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
