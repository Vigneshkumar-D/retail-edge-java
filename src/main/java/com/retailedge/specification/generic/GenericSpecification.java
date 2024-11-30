package com.retailedge.specification.generic;


import com.retailedge.specification.SearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.Date;

public class GenericSpecification<T> implements Specification<T> {
    protected SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    private Path<?> getPath(Root<T> root, String key) {
        if (key.contains(".")) {
            String[] keys = key.split("\\.");
            Path<?> path = root.get(keys[0]);
            for (int i = 1; i < keys.length; i++) {
                path = path.get(keys[i]);
            }
            return path;
        } else {
            return root.get(key);
        }
    }


    //    @Override
//    public Predicate toPredicate
//            (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//
//        if (criteria.getOperation().equalsIgnoreCase(">=")) {
//            if (root.get(criteria.getKey()).getJavaType() == Date.class) {
//                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),(Date) criteria.getValue());
//            }
//            else if(root.get(criteria.getKey()).getJavaType() == Instant.class){
//                return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),(Instant) criteria.getValue());
//            }
//            else{
//                return builder.greaterThanOrEqualTo(
//                        root.<String>get(criteria.getKey()), criteria.getValue().toString());
//            }
//        }
//        else if (criteria.getOperation().equalsIgnoreCase(">")) {
//            return builder.greaterThan(
//                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
//        }
//        else if (criteria.getOperation().equalsIgnoreCase("<=")) {
//            if (root.get(criteria.getKey()).getJavaType() == Date.class) {
//                return builder.lessThanOrEqualTo(
//                        root.get(criteria.getKey()),(Date) criteria.getValue());
//            }
//            else if(root.get(criteria.getKey()).getJavaType() == Instant.class){
//                return builder.lessThanOrEqualTo(root.get(criteria.getKey()),(Instant) criteria.getValue());
//            }
//            else{
//                return builder.lessThanOrEqualTo(
//                        root.<String>get(criteria.getKey()), criteria.getValue().toString());
//
//            }
//        }
//        else if (criteria.getOperation().equalsIgnoreCase("<")) {
//            return builder.lessThan(
//                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
//        }
//        else if (criteria.getOperation().equalsIgnoreCase(":")) {
//            if (root.get(criteria.getKey()).getJavaType() == String.class) {
//                return builder.like(
//                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
//            }
//            else if(criteria.getValue()==null){
//                return builder.isNull(root.get(criteria.getKey()));
//            }
//            else {
//                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
//            }
//
//
//        }
//        else if (criteria.getOperation().equalsIgnoreCase ("!:")) {
//            if (root.get(criteria.getKey()).getJavaType() == String.class) {
//                return builder.notEqual(
//                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
//            }
//
//            else {
//                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
//            }
//        }
//        else if (criteria.getOperation().equalsIgnoreCase ("in")) {
//            if (root.get(criteria.getKey()).getJavaType() == Integer.class) {
//                CriteriaBuilder.In<Integer> inClause = builder.in(root.get(criteria.getKey()));
//                int n = Array.getLength(criteria.getValue());
//                for (int i=0;i<n;i++) {
//                    int value = (int) Array.get(criteria.getValue(),i);
//                    inClause.value(value);
//                }
//                return inClause;
//            }
//            else  if (root.get(criteria.getKey()).getJavaType() == String.class) {
//
//                CriteriaBuilder.In<String> inClause = builder.in(root.get(criteria.getKey()));
//                int n = Array.getLength(criteria.getValue());
//                for (int i=0;i<n;i++) {
//                    String value = (String) Array.get(criteria.getValue(),i);
//                    inClause.value(value);
//                }
//                return inClause;
//            }
//        }
//
//        return null;
//    }
@Override
public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    Path<?> path = getPath(root, criteria.getKey());

    switch (criteria.getOperation().toLowerCase()) {
        case ">=":
            if (path.getJavaType() == Date.class) {
                return builder.greaterThanOrEqualTo(path.as(Date.class), (Date) criteria.getValue());
            } else if (path.getJavaType() == Instant.class) {
                return builder.greaterThanOrEqualTo(path.as(Instant.class), (Instant) criteria.getValue());
            } else {
                return builder.greaterThanOrEqualTo(path.as(String.class), criteria.getValue().toString());
            }
        case ">":
            return builder.greaterThan(path.as(String.class), criteria.getValue().toString());
        case "<=":
            if (path.getJavaType() == Date.class) {
                return builder.lessThanOrEqualTo(path.as(Date.class), (Date) criteria.getValue());
            } else if (path.getJavaType() == Instant.class) {
                return builder.lessThanOrEqualTo(path.as(Instant.class), (Instant) criteria.getValue());
            } else {
                return builder.lessThanOrEqualTo(path.as(String.class), criteria.getValue().toString());
            }
        case "<":
            return builder.lessThan(path.as(String.class), criteria.getValue().toString());
        case ":":
            if (path.getJavaType() == String.class) {
                return builder.like(path.as(String.class), "%" + criteria.getValue() + "%");
            } else if (criteria.getValue() == null) {
                return builder.isNull(path);
            } else {
                return builder.equal(path, criteria.getValue());
            }
        case "!:":
            if (path.getJavaType() == String.class) {
                return builder.notLike(path.as(String.class), "%" + criteria.getValue() + "%");
            } else {
                return builder.notEqual(path, criteria.getValue());
            }
        case "in":
            CriteriaBuilder.In<Object> inClause = builder.in(path);
            int n = Array.getLength(criteria.getValue());
            for (int i = 0; i < n; i++) {
                inClause.value(Array.get(criteria.getValue(), i));
            }
            return inClause;
        default:
            throw new IllegalArgumentException("Operation " + criteria.getOperation() + " is not supported.");
    }
}

}
