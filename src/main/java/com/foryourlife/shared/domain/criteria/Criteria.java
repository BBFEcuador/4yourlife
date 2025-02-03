package com.foryourlife.shared.domain.criteria;

import java.util.List;
import java.util.Optional;

public class Criteria {
    public final List<Filter> filters;
    public final Optional<Integer> limit;
    public final Optional<Integer> offset;

    public Criteria(List<Filter> filters, Optional<Integer> limit, Optional<Integer> offset) {
        this.filters = filters;
        this.limit = limit;
        this.offset = offset;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public Optional<Integer> getLimit() {
        return limit;
    }

    public Optional<Integer> getOffset() {
        return offset;
    }
}
