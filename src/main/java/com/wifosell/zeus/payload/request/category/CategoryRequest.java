package com.wifosell.zeus.payload.request.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 50)
    private String shortName;

    private Long parentCategoryId;

    private Boolean active;
}
