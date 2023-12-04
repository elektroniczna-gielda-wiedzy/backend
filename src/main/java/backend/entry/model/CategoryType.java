package backend.entry.model;

import java.util.Arrays;
import java.util.Optional;

public enum CategoryType {
    DEPARTMENT(0),
    FIELD(1);

    private final int value;

    CategoryType(final int value) {
        this.value = value;
    }

    public static Optional<CategoryType> valueOf(int value) {
        return Arrays.stream(values())
                .filter(ct -> ct.value == value)
                .findFirst();
    }

    public Integer getId() {
        return value;
    }
}
