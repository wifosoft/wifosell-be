package com.wifosell.zeus.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class ZeusUtils {
    public static Pageable getDefaultPageable(Integer offset, Integer limit, String sortBy, String orderBy) {
        if (offset == null) offset = 0;
        if (limit == null) limit = 10;
        if (orderBy == null) orderBy = Sort.Direction.ASC.name();
        Sort sort = sortBy == null ? Sort.unsorted() : Sort.by(Sort.Direction.fromString(orderBy), sortBy);
        return PageRequest.of(offset, limit, sort);
    }

    public static int convertIndicesToSortedIndex(List<Integer> indices, List<Integer> sizes) {
        if (indices.isEmpty())
            return 0;

        int factor = 1;
        int index = indices.get(indices.size() - 1);

        for (int i = indices.size() - 1; i > 0; --i) {
            factor *= sizes.get(i);
            index += factor * indices.get(i - 1);
        }

        return index;
    }
}
