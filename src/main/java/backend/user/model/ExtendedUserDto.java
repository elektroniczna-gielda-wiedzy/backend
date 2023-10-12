package backend.user.model;

import backend.adapter.rest.model.common.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Getter
@Setter
@Builder
@Jacksonized
public class ExtendedUserDto {

    @JsonProperty("basic_info")
    private UserDto userInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("email")
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("last_login")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date lastLogin;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("activity")
    private ActivityInfo activityInfo;
}
