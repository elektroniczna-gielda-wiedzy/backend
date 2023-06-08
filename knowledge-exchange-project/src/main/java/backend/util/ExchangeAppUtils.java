package backend.util;

import java.util.Arrays;
import java.util.List;

public class ExchangeAppUtils {

    public static List<Integer> convertCategoriesStrToList(String list) {
        String cleanList = list.replace("[","").replace("]","");
        String[] categoriesArr = cleanList.split(",");
        return Arrays.stream(categoriesArr).map(
                Integer::parseInt
        ).toList();
    }
}
