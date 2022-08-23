package com.wifosell.zeus.payload.request.common;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SearchRequest {
    @NotEmpty
    String keyword;
}
