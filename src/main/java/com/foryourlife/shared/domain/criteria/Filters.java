package com.foryourlife.shared.domain.criteria;

import java.util.List;

public class Filters {
    private final List<Filter> filters;

    public Filters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Filter> getFilters() {
        return filters;
    }
}
