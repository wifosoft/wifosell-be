package com.wifosell.zeus.payload.response.option;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OptionResponse extends BasicEntityResponse {
    private final String name;
    private final List<String> values;

    public OptionResponse(OptionModel option) {
        super(option);
        this.name = option.getName();
        this.values = option.getOptionValues().stream().map(OptionValue::getValue).collect(Collectors.toList());
    }
}
