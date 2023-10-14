package backend.entry.model;

import java.util.Arrays;
import java.util.Optional;

public enum CategoryStatus {
    ACTIVE(0),
    SUGGESTED(1),
    DELETED(2);

    private final int value;

    CategoryStatus(final int value) {
        this.value = value;
    }

    public static Optional<CategoryStatus> valueOf(int value) {
        return Arrays.stream(values())
                .filter(cs -> cs.value == value)
                .findFirst();
    }
    public static Optional<CategoryStatus> valueOfLabel(String value) {
        return Arrays.stream(values())
                .filter(cs -> cs.name().equals(value))
                .findFirst();
    }
}
