package com.foryourlife.shared.infrastructure.criteria;

import com.foryourlife.shared.domain.criteria.Criteria;
import com.foryourlife.shared.domain.criteria.Filter;
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

    public Pageable getJpaPageable(Criteria criteria){
        return PageRequest.of(criteria.getLimit().get(),criteria.getOffset().get());
    }

    public Specification<T> getJpaSpecifications(Criteria criteria) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            for (Filter filter : criteria.getFilters()) {

                switch (filter.getOperation()) {

                    case EQUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(filter.getColumn()), filter.getValue());
                        predicates.add(equal);
                        break;

                    case LIKE:
                        Predicate like = criteriaBuilder.like(root.get(filter.getColumn()), "%" + filter.getValue() + "%");
                        predicates.add(like);
                        break;

                    case IN:
                        String[] split = filter.getValue().split(",");
                        Predicate in = root.get(filter.getColumn()).in(Arrays.asList(split));
                        predicates.add(in);
                        break;

                    case GREATER_THAN:
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(filter.getColumn()), filter.getValue());
                        predicates.add(greaterThan);
                        break;

                    case LESS_THAN:
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(filter.getColumn()), filter.getValue());
                        predicates.add(lessThan);
                        break;

                    case BETWEEN:
                        //"10, 20"
                        String[] split1 = filter.getValue().split(",");
                        Predicate between = criteriaBuilder.between(root.get(filter.getColumn()), Long.parseLong(split1[0]), Long.parseLong(split1[1]));
                        predicates.add(between);
                        break;

                    case JOIN:
                        Predicate join = criteriaBuilder.equal(root.join(filter.getJoinTable()).get(filter.getColumn()), filter.getValue());
                        predicates.add(join);
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + "");
                }

            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
