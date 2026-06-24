package com.propertyproject.propertyservice.common.dto;

import org.springframework.data.domain.Page;

import java.util.List; 

/**
 * Stable pagination envelope so the public API does not leak Spring Data's
 * Page representation (which is unstable across versions).
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
