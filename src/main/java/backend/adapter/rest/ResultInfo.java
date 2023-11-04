package backend.adapter.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResultInfo {
    @JsonProperty("page")
    private Integer page;

    @JsonProperty("per_page")
    private Integer perPage;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("total_count")
    private Integer totalCount;

    public static ResultInfo buildFromPaginator(Paginator<?> paginator) {
        return ResultInfo.builder()
                .page(paginator.getPage())
                .perPage(paginator.getPerPage())
                .count(paginator.getCount())
                .totalCount(paginator.getTotalCount())
                .build();
    }
}
