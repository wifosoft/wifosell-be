package com.wifosell.zeus.payload.request.option;

import com.wifosell.zeus.model.product.OptionValue;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class OptionRequest {
    @NotBlank
    private String name;

    private List<OptionValue> optionValues;
}
