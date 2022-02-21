package com.wifosell.zeus.payload.request.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class DeactivateCategoriesRequest {
    @NotEmpty
    List<Long> ids;
}
