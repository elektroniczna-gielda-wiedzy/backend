package backend.util;

import backend.model.dao.ChatDao;
import backend.model.dao.UserDao;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class ExchangeAppUtils {
    public static String REQUIRED_VALUE_ERROR_FORMAT = "field %s is required";

    public static String BAD_EMAIL_ERROR = "The email does not come from authorized entity";

    public static List<Integer> convertCategoriesStrToList(String list) {
        String cleanList = list.replace("[", "").replace("]", "");
        String[] categoriesArr = cleanList.split(",");
        return Arrays.stream(categoriesArr).map(Integer::parseInt).toList();
    }

    public static UserDao getOppositeUser(Integer currentUserId, ChatDao chatDao) {
        if (currentUserId.equals(chatDao.getUserOneDao().getId())) {
            return chatDao.getUserTwoDao();
        } else {
            return chatDao.getUserOneDao();
        }
    }

    public static UserDao getCurrentUserDao(Integer currentUserId, ChatDao chatDao) {
        if (currentUserId.equals(chatDao.getUserOneDao().getId())) {
            return chatDao.getUserOneDao();
        } else {
            return chatDao.getUserTwoDao();
        }
    }

    public static Timestamp getCurrentUserLastRead(Integer currentUserId, ChatDao chatDao) {
        if (currentUserId.equals(chatDao.getUserOneDao().getId())) {
            return chatDao.getUserOneLastRead();
        } else {
            return chatDao.getUserTwoLastRead();
        }
    }
}
