package com.foryourlife.shared.infrastructure.criteria;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
import com.foryourlife.shared.domain.exception.BaseException;
import com.foryourlife.shared.domain.level.CourseLevel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(join.get(filter.getColumn())),
                                    "%" + filter.getValue().toLowerCase() + "%"
                            );
                    case IN -> {
                        String[] split = filter.getValue().split(",");
                        yield join.get(filter.getColumn()).in(Arrays.asList(split));
                    }
                    case GREATER_THAN -> criteriaBuilder.greaterThan(join.get(filter.getColumn()), filter.getValue());
                    case LESS_THAN -> criteriaBuilder.lessThan(join.get(filter.getColumn()), filter.getValue());
                    case BETWEEN -> {
                        String[] parts = filter.getValue().split(",");
                        String start = parts[0];
                        String end = parts[1];

                        Expression<? extends Comparable> path =
                                join.get(filter.getColumn());

                        Comparable convertedStart =
                                (Comparable) convertValue(start, path.getJavaType());

                        Comparable convertedEnd =
                                (Comparable) convertValue(end, path.getJavaType());

                        yield criteriaBuilder.between(
                                path,
                                convertedStart,
                                convertedEnd
                        );
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
                    case IS_TRUE -> criteriaBuilder.isTrue(join.get(filter.getColumn()));
                    case IS_FALSE -> criteriaBuilder.isFalse(join.get(filter.getColumn()));
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

    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value, Class<T> targetType) {

        if (targetType == String.class) {
            return (T) value;
        }

        if (targetType == Integer.class || targetType == int.class) {
            return (T) Integer.valueOf(value);
        }

        if (targetType == Long.class || targetType == long.class) {
            return (T) Long.valueOf(value);
        }

        if (targetType == Double.class || targetType == double.class) {
            return (T) Double.valueOf(value);
        }

        if (targetType == Float.class || targetType == float.class) {
            return (T) Float.valueOf(value);
        }

        if (targetType == Boolean.class || targetType == boolean.class) {
            return (T) Boolean.valueOf(value);
        }

        if (targetType == BigDecimal.class) {
            return (T) new BigDecimal(value);
        }

        if (targetType == LocalDate.class) {
            return (T) LocalDate.parse(value);
        }

        if (targetType == LocalDateTime.class) {
            try {
                return (T) LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException ex) {
                // fallback: si solo viene la fecha -> se asume 00:00:00
                return (T) LocalDate
                        .parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay();
            }
        }

        // fallback por defecto
        return (T) value;
    }
}
