package backend.adapter.rest;

import lombok.Getter;

import java.util.List;

@Getter
public class Paginator<T> {
    private final List<T> result;

    private final Integer page;

    private final Integer perPage;

    private final Integer count;

    private final Integer totalCount;

    public Paginator(List<T> result, Integer page, Integer perPage) {
        int start = Math.min((page - 1) * perPage, result.size());
        int end = Math.min(start + perPage, result.size());

        this.result = result.subList(start, end);
        this.page = page;
        this.perPage = perPage;
        this.count = this.result.size();
        this.totalCount = result.size();
    }
}
