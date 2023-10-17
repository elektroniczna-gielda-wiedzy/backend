package backend.adapter.rest.model.entry;

import backend.entry.model.EntryType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Setter
@Getter
@Jacksonized
public class EntryTypeDto {
    @JsonProperty("entry_type_id")
    private Integer entryId;

    @JsonProperty("name")
    private String name;

    public static EntryTypeDto buildFromModel(EntryType entryType) {
        return EntryTypeDto.builder()
                .entryId(entryType.getId())
                .name(entryType.getName())
                .build();
    }
}
