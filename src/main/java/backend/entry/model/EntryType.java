package backend.entry.model;

import java.util.Arrays;
import java.util.Optional;

public enum EntryType {
    NOTE(0, "note"),
    ANNOUNCEMENT(1, "announcement"),
    POST(2, "post");

    private final Integer id;

    private final String name;

    EntryType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<EntryType> valueOf(Integer id) {
        return Arrays.stream(values())
                .filter(type -> type.id.equals(id))
                .findFirst();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

