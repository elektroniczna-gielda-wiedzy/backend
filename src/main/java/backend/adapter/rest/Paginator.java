package backend.adapter.rest;

import lombok.Getter;

import java.util.List;

@Getter
public class Paginator<T> {
    public static final Integer DEFAULT_PAGE = 1;

    public static final Integer DEFAULT_PER_PAGE = 20;

    private final List<T> result;

    private final Integer page;

    private final Integer perPage;

    private final Integer count;

    private final Integer totalCount;

    public Paginator(List<T> result, Integer page, Integer perPage) {
        if (page == null) {
            page = DEFAULT_PAGE;
        }

        if (perPage == null) {
            perPage = DEFAULT_PER_PAGE;
        }

        int start = Math.min((page - 1) * perPage, result.size());
        int end = Math.min(start + perPage, result.size());

        this.result = result.subList(start, end);
        this.page = page;
        this.perPage = perPage;
        this.count = this.result.size();
        this.totalCount = result.size();
    }
}
