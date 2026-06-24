package com.propertyproject.propertyservice.property;

import com.propertyproject.propertyservice.common.dto.PageResponse;
import com.propertyproject.propertyservice.property.dto.PropertyRequest;
import com.propertyproject.propertyservice.property.dto.PropertyResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping
    public PageResponse<PropertyResponse> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String city,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return PageResponse.from(propertyService.searchProperties(q, type, city, pageable));
    }

    @GetMapping("/{id}")
    public PropertyResponse get(@PathVariable Long id) {
        return propertyService.getProperty(id);
    }

    @PostMapping
    public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest request,
                                                   UriComponentsBuilder uri) {
        PropertyResponse created = propertyService.createProperty(request);
        URI location = uri.path("/api/properties/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public PropertyResponse update(@PathVariable Long id, @Valid @RequestBody PropertyRequest request) {
        return propertyService.updateProperty(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        propertyService.deleteProperty(id);
    }
}
