package com.foryourlife.shared.domain.criteria;

import java.util.List;
import java.util.Optional;

public class Criteria {
    private final Filters           filters;
    private final Optional<Integer> limit;
    private final Optional<Integer> offset;

    public Criteria(Filters filters, Optional<Integer> limit, Optional<Integer> offset) {
        this.filters = filters;
        this.limit = limit;
        this.offset = offset;
    }

    public List<Filter> getFilters() {
        return filters.getFilters();
    }

    public Optional<Integer> getLimit() {
        return limit;
    }

    public Optional<Integer> getOffset() {
        return offset;
    }
}
