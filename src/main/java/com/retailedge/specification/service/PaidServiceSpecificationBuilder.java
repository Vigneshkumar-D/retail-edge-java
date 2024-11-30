package com.retailedge.specification.service;

import com.retailedge.entity.service.PaidService;
import com.retailedge.specification.generic.GenericSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class PaidServiceSpecificationBuilder extends GenericSpecificationBuilder<PaidService> {

    @Override
    public Specification<PaidService> build() {
        if (params.isEmpty()) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(PaidServiceSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }

        return result;
    }
}
