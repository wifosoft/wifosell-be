package com.wifosell.zeus.payload.response.category;

import com.wifosell.zeus.model.category.Category;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetCategoriesResponse extends CategoryResponse {
    private final List<GetCategoriesResponse> children;

    public GetCategoriesResponse(@NonNull Category category, Boolean isActive) {
        super(category);
        if (isActive == null) {
            this.children = category.getChildren().stream()
                    .map(c -> new GetCategoriesResponse(c, isActive))
                    .collect(Collectors.toList());
        } else {
            this.children = category.getChildren().stream()
                    .filter(c -> c.getIsActive() == isActive)
                    .map(c -> new GetCategoriesResponse(c, isActive))
                    .collect(Collectors.toList());
        }
    }
}
