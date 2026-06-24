package com.propertyproject.propertyservice.property;

import com.propertyproject.propertyservice.common.exception.ResourceNotFoundException;
import com.propertyproject.propertyservice.property.dto.PropertyMapper;
import com.propertyproject.propertyservice.property.dto.PropertyRequest;
import com.propertyproject.propertyservice.property.dto.PropertyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@Transactional(readOnly = true)
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyService(PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    public Page<PropertyResponse> searchProperties(String q, String type, String city, Pageable pageable) {
        Specification<Property> spec = Specification.unrestricted();

        if (StringUtils.hasText(q)) {
            spec = spec.and(PropertyRepository.nameContains(q));
        }
        if (StringUtils.hasText(type)) {
            spec = spec.and(PropertyRepository.hasType(PropertyType.valueOf(type.toUpperCase())));
        }
        if (StringUtils.hasText(city)) {
            spec = spec.and(PropertyRepository.inCity(city));
        }

        Page<Property> propertiesPage = propertyRepository.findAll(spec, pageable);
        return propertiesPage.map(propertyMapper::toResponse);
    }

    @Transactional
    public PropertyResponse getProperty(Long id) {
        Property property = propertyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property", id));
        return propertyMapper.toResponse(property);
    }

    @Transactional
    public PropertyResponse createProperty(PropertyRequest request) {
        Property newProperty = propertyMapper.toEntity(request);
        Property savedProperty = propertyRepository.save(newProperty);
        return propertyMapper.toResponse(savedProperty);
    }

    @Transactional
    public PropertyResponse updateProperty(Long id, PropertyRequest request) {
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property", id));

        propertyMapper.updateEntity(existingProperty, request);
        return propertyMapper.toResponse(existingProperty);
    }

    @Transactional
    public void deleteProperty(Long id) {
        if (!propertyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Property", id);
        }
        propertyRepository.deleteById(id);
    }
}