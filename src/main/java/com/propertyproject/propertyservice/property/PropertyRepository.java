package com.propertyproject.propertyservice.property;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PropertyRepository 
        extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    
    static Specification<Property> nameContains(String q) {
        return (root, query, cb) ->
            cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%");
    }

    static Specification<Property> hasType(PropertyType type) {
        return (root, query, cb) -> cb.equal(root.get("propertyType"), type);
    }

    static Specification<Property> inCity(String city) {
        return (root, query, cb) -> cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }
}
