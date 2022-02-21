package com.wifosell.zeus.payload.request.shop;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class DeactivateShopsRequest {
    @NotEmpty
    List<Long> ids;
}
