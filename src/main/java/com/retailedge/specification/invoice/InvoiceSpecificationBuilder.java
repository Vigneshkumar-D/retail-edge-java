package com.retailedge.specification.invoice;

import com.retailedge.entity.invoice.Invoice;
import com.retailedge.specification.generic.GenericSpecificationBuilder;
import com.retailedge.specification.service.PaidServiceSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;


public class InvoiceSpecificationBuilder extends GenericSpecificationBuilder<Invoice> {

    @Override
    public Specification<Invoice> build() {
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
