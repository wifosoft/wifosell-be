package com.wifosell.zeus.utils.paging;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class PageInfo<T> {
    List<T> content;
    int offset;
    int limit;
    long totalElements;

    public PageInfo(List<T> content, int offset, int limit, long totalElements) {
        this.content = content;
        this.offset = offset;
        this.limit = limit;
        this.totalElements = totalElements;
    }

    public <U> PageInfo<U> map(Function<? super T, ? extends U> func) {
        return new PageInfo<>(
                this.content.stream().map(func).collect(Collectors.toList()),
                this.offset,
                this.limit,
                this.totalElements
        );
    }
}
