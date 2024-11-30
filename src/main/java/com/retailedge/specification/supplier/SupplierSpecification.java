package com.retailedge.specification.supplier;
//
import com.retailedge.entity.suppiler.Supplier;
//import org.springframework.data.jpa.domain.Specification;
//
//public class SupplierSpecification {
//
//    public static Specification<Supplier> hasSupplierId(Integer supplierId) {
//        return (root, query, criteriaBuilder) -> {
//            if (supplierId == null) {
//                return criteriaBuilder.conjunction();
//            }
//            return criteriaBuilder.equal(root.get("id"), supplierId);
//        };
//    }
//}

//package com.retailedge.entity.supplier;

import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification {

    public static Specification<Supplier> hasSupplierId(Integer supplierId) {
        return (root, query, criteriaBuilder) -> {
            if (supplierId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), supplierId);
        };
    }
    public static Specification<Supplier> hasSupplierName(String supplierName) {
        return (root, query, builder) -> supplierName == null ? builder.conjunction()
                : builder.like(builder.lower(root.get("supplierName")), "%" + supplierName.toLowerCase() + "%");
    }

    public static Specification<Supplier> hasTotalOrderValue(Double totalOrderValue) {
        return (root, query, builder) -> totalOrderValue == null ? builder.conjunction()
                : builder.equal(root.get("totalOrderValue"), totalOrderValue);
    }

    public static Specification<Supplier> hasPaidTotal(Double paidTotal) {
        return (root, query, builder) -> paidTotal == null ? builder.conjunction()
                : builder.equal(root.get("paidTotal"), paidTotal);
    }

    public static Specification<Supplier> hasBalance(Double balance) {
        return (root, query, builder) -> balance == null ? builder.conjunction()
                : builder.equal(root.get("balance"), balance);
    }

    public static Specification<Supplier> hasLastPayment(Double lastPayment) {
        return (root, query, builder) -> lastPayment == null ? builder.conjunction()
                : builder.equal(root.get("lastPayment"), lastPayment);
    }

    public static Specification<Supplier> hasContactName(String contactName) {
        return (root, query, builder) -> contactName == null ? builder.conjunction()
                : builder.like(builder.lower(root.get("contactName")), "%" + contactName.toLowerCase() + "%");
    }

    public static Specification<Supplier> hasContactEmail(String contactEmail) {
        return (root, query, builder) -> contactEmail == null ? builder.conjunction()
                : builder.like(builder.lower(root.get("contactEmail")), "%" + contactEmail.toLowerCase() + "%");
    }

    public static Specification<Supplier> hasContactPhone(String contactPhone) {
        return (root, query, builder) -> contactPhone == null ? builder.conjunction()
                : builder.like(builder.lower(root.get("contactPhone")), "%" + contactPhone.toLowerCase() + "%");
    }

    public static Specification<Supplier> hasAddress(String address) {
        return (root, query, builder) -> address == null ? builder.conjunction()
                : builder.like(builder.lower(root.get("address")), "%" + address.toLowerCase() + "%");
    }

    public static Specification<Supplier> buildSpecification(Supplier filter) {
        Specification<Supplier> spec = Specification.where(null);

        if (filter.getSupplierName() != null) {
            spec = spec.and(hasSupplierName(filter.getSupplierName()));
        }
        if (filter.getTotalOrderValue() != null) {
            spec = spec.and(hasTotalOrderValue(filter.getTotalOrderValue()));
        }
        if (filter.getPaidTotal() != null) {
            spec = spec.and(hasPaidTotal(filter.getPaidTotal()));
        }
        if (filter.getBalance() != null) {
            spec = spec.and(hasBalance(filter.getBalance()));
        }
        if (filter.getLastPayment() != null) {
            spec = spec.and(hasLastPayment(filter.getLastPayment()));
        }
        if (filter.getContactName() != null) {
            spec = spec.and(hasContactName(filter.getContactName()));
        }
        if (filter.getContactEmail() != null) {
            spec = spec.and(hasContactEmail(filter.getContactEmail()));
        }
        if (filter.getContactPhone() != null) {
            spec = spec.and(hasContactPhone(filter.getContactPhone()));
        }
        if (filter.getAddress() != null) {
            spec = spec.and(hasAddress(filter.getAddress()));
        }

        return spec;
    }
}

