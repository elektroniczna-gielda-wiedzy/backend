package backend.entry.model;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum EntrySortMode {
    CREATED_DATE(0),
    MODIFIED_DATE(1),
    VOTES(2);

    private static final Map<Integer, EntrySortMode> enumMap =
            Arrays.stream(EntrySortMode.values()).collect(Collectors.toMap(EntrySortMode::getValue, Function.identity())
    );

    EntrySortMode(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }

    public static EntrySortMode getModeByValue(Integer value) {
        return enumMap.get(value);
    }
}
