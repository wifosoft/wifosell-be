package com.wifosell.zeus.payload.response.category;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryResponse extends BasicEntityResponse {
    private final Long id;
    private final String name;
    private final String shortName;
    private final String description;
    private final Category parent;
    private final List<Category> children;

    public CategoryResponse(Category category) {
        super(category);
        this.id = category.getId();
        this.name = category.getName();
        this.shortName = category.getShortName();
        this.description = category.getDescription();
        this.parent = category.getParent();
        this.children = category.getChildren();
    }
}
