package com.propertyproject.propertyservice.property.dto;

import org.springframework.stereotype.Component;

import com.propertyproject.propertyservice.property.Property; 

@Component
public class PropertyMapper {

    public Property toEntity(PropertyRequest req) {
        Property p = new Property();
        updateEntity(p, req);
        return p;
    }

    // entity first, request second — matches your service's updateProperty call
    public void updateEntity(Property p, PropertyRequest req) {
        p.setName(req.name());
        p.setAddressLine1(req.addressLine1());
        p.setCity(req.city());
        p.setState(req.state());
        p.setPostalCode(req.postalCode());
        p.setPropertyType(req.propertyType());
        p.setSquareFeet(req.squareFeet());
        p.setYearBuilt(req.yearBuilt());
        p.setAcquiredOn(req.acquiredOn());
        p.setAcquisitionPrice(req.acquisitionPrice());
    }

    public PropertyResponse toResponse(Property p) {
        return new PropertyResponse(
                p.getId(), p.getName(), p.getAddressLine1(), p.getCity(), p.getState(), p.getPostalCode(),
                p.getPropertyType(), p.getSquareFeet(), p.getYearBuilt(),
                p.getAcquiredOn(), p.getAcquisitionPrice(), p.getCreatedAt(), p.getUpdatedAt());
    }
}
