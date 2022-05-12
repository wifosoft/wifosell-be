package com.wifosell.zeus.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ZeusUtils {
    public static Pageable getDefaultPageable(Integer offset, Integer limit, String sortBy, String orderBy) {
        if (offset == null) offset = 0;
        if (limit == null) limit = 10;
        if (orderBy == null) orderBy = Sort.Direction.ASC.name();
        Sort sort = sortBy == null ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(orderBy), sortBy);
        return PageRequest.of(offset, limit, sort);
    }
}
