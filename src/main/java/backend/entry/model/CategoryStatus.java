package backend.entry.model;

import java.util.Arrays;
import java.util.Optional;

public enum CategoryStatus {
    ACTIVE(0, "active"),
    SUGGESTED(1, "suggested"),
    DELETED(2, "deleted");

    private final Integer id;

    private final String name;

    CategoryStatus(final Integer id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<CategoryStatus> valueOf(int id) {
        return Arrays.stream(values())
                .filter(cs -> cs.id == id)
                .findFirst();
    }
    public static Optional<CategoryStatus> valueOfLabel(String value) {
        return Arrays.stream(values())
                .filter(cs -> cs.name().equals(value))
                .findFirst();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
