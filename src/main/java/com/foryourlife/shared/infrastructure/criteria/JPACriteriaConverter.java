package com.foryourlife.shared.infrastructure.criteria;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JPACriteriaConverter<T> {

    public Pageable getJpaPageable(Criteria criteria) {
        return PageRequest.of(criteria.getLimit().get(), criteria.getOffset().get());
    }

    public Specification<T> getJpaSpecifications(Criteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> orPredicates = new ArrayList<>();

            for (Filter filter : criteria.getFilters()) {
                Predicate predicate = switch (filter.getOperation()) {
                    case EQUAL -> criteriaBuilder.equal(root.get(filter.getColumn()), filter.getValue());
                    case LIKE -> criteriaBuilder.like(root.get(filter.getColumn()), "%" + filter.getValue() + "%");
                    case IN -> {
                        String[] split = filter.getValue().split(",");
                        yield root.get(filter.getColumn()).in(Arrays.asList(split));
                    }
                    case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(filter.getColumn()), filter.getValue());
                    case LESS_THAN -> criteriaBuilder.lessThan(root.get(filter.getColumn()), filter.getValue());
                    case BETWEEN -> {
                        String[] split1 = filter.getValue().split(",");
                        yield criteriaBuilder.between(root.get(filter.getColumn()), Long.parseLong(split1[0]), Long.parseLong(split1[1]));
                    }
                    case JOIN ->
                            criteriaBuilder.equal(root.join(filter.getJoinTable()).get(filter.getColumn()), filter.getValue());
                    default -> throw new IllegalStateException("Unexpected value: " + "");
                };

                if (filter.getLogicalOperator() == Filter.LogicalOperator.OR) {
                    orPredicates.add(predicate);
                } else {
                    andPredicates.add(predicate);
                }

            }
            Predicate finalPredicate = null;

            if (!andPredicates.isEmpty() && !orPredicates.isEmpty()) {
                finalPredicate = criteriaBuilder.and(
                        criteriaBuilder.and(andPredicates.toArray(new Predicate[0])),
                        criteriaBuilder.or(orPredicates.toArray(new Predicate[0]))
                );
            } else if (!andPredicates.isEmpty()) {
                finalPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
            } else if (!orPredicates.isEmpty()) {
                finalPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
            }
            return finalPredicate;
        };
    }
}
