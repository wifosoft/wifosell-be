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
    private final List<OptionValueResponse> values;

    public OptionResponse(OptionModel option) {
        super(option);
        this.name = option.getName();
        this.values = option.getOptionValues().stream()
                .filter(optionValue -> !optionValue.isDeleted())
                .map(OptionValueResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class OptionValueResponse extends BasicEntityResponse {
        private final String name;

        public OptionValueResponse(OptionValue optionValue) {
            super(optionValue);
            this.name = optionValue.getName();
        }
    }
}
