package com.wifosell.zeus.payload.response.category;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryResponse extends BasicEntityResponse {
    private final Long id;
    private final String name;
    private final String shortName;
    private final String description;
    private final Long parentId;
    private final List<CategoryResponse> children;

    public CategoryResponse(@NonNull Category category) {
        super(category);
        this.id = category.getId();
        this.name = category.getName();
        this.shortName = category.getShortName();
        this.description = category.getDescription();
        this.parentId = category.getParent() != null ? category.getParent().getId() : null;
        this.children = category.getChildren().stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    public CategoryResponse(@NonNull Category category, @NonNull Boolean isActive) {
        super(category);
        this.id = category.getId();
        this.name = category.getName();
        this.shortName = category.getShortName();
        this.description = category.getDescription();
        this.parentId = category.getParent() != null ? category.getParent().getId() : null;
        this.children = category.getChildren().stream()
                .filter(c -> c.isActive() == isActive)
                .map(c -> new CategoryResponse(c, isActive))
                .collect(Collectors.toList());
    }
}
