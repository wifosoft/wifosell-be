package com.wifosell.zeus.payload.response.category;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CategoryResponse extends BasicEntityResponse {
    private final String name;
    private final String shortName;
    private final String description;

    public CategoryResponse(@NonNull Category category) {
        super(category);
        this.name = category.getName();
        this.shortName = category.getShortName();
        this.description = category.getDescription();
    }
}
