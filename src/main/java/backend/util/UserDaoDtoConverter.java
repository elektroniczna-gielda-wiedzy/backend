package backend.util;

import backend.model.dao.UserDao;
import backend.model.dto.UserDto;

public class UserDaoDtoConverter {
    public static UserDto convertToDto(UserDao userDao) {
        return UserDto.builder()
                .userId(userDao.getId())
                .firstName(userDao.getFirstName())
                .lastName(userDao.getLastName())
                .build();
    }
}
