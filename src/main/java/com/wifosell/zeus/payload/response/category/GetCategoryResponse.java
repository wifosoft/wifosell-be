package com.wifosell.zeus.payload.response.category;

import com.wifosell.zeus.model.category.Category;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class GetCategoryResponse extends CategoryResponse {
    private final CategoryResponse parent;

    public GetCategoryResponse(@NonNull Category category) {
        super(category);
        this.parent = category.getParent() != null ? new CategoryResponse(category.getParent()) : null;
    }
}
