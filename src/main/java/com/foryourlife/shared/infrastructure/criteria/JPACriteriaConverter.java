package com.foryourlife.shared.infrastructure.criteria;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
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
        return PageRequest.of(criteria.getLimit().orElseThrow(() -> new BaseException("Not limit given", List.of("You must need to provide a limit"))), criteria.getOffset().orElseThrow(() -> new BaseException("Not offset given", List.of("You must need to provide a offset"))));
    }

    public Specification<T> getJpaSpecifications(Criteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> andPredicates = new ArrayList<>();
            List<Predicate> orPredicates = new ArrayList<>();

            for (Filter filter : criteria.getFilters()) {
                From<?, ?> join = root;
                if (filter.getJoinTable() != null && !filter.getJoinTable().isEmpty()) {
                    for (String table : filter.getJoinTable().split("\\.")) {
                        join = join.join(table, JoinType.LEFT);
                    }
                }
                Predicate predicate = switch (filter.getOperation()) {
                    case EQUAL ->
                            criteriaBuilder.equal(join.get(filter.getColumn()), criteriaBuilder.literal(filter.getValue()));
                    case LIKE ->
                            criteriaBuilder.like(join.get(filter.getColumn()), criteriaBuilder.literal("%" + filter.getValue() + "%"));
                    case IN -> {
                        String[] split = filter.getValue().split(",");
                        yield join.get(filter.getColumn()).in(Arrays.asList(split));
                    }
                    case GREATER_THAN -> criteriaBuilder.greaterThan(join.get(filter.getColumn()), filter.getValue());
                    case LESS_THAN -> criteriaBuilder.lessThan(join.get(filter.getColumn()), filter.getValue());
                    case BETWEEN -> {
                        String[] split1 = filter.getValue().split(",");
                        yield criteriaBuilder.between(join.get(filter.getColumn()), Long.parseLong(split1[0]), Long.parseLong(split1[1]));
                    }
                    case JOIN -> {
                        yield criteriaBuilder.equal(join.get(filter.getColumn()), criteriaBuilder.literal(filter.getValue()));
                    }
                    case GET_LAST -> {
                        query.orderBy(criteriaBuilder.desc(join.get(filter.getColumn())));
                        yield criteriaBuilder.conjunction();
                    }
                    case IS_NULL -> criteriaBuilder.isNull(join.get(filter.getColumn()));
                    case IS_NOT ->
                            criteriaBuilder.notEqual(join.get(filter.getColumn()), criteriaBuilder.literal(filter.getValue()));
                    case IS_EMPTY -> criteriaBuilder.isEmpty(join.get(filter.getColumn()));
                    default -> throw new IllegalStateException("Unexpected value: ");
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
