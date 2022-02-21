package com.wifosell.zeus.payload.request.option;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class OptionRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private List<OptionValueRequest> optionValueRequests;

    @Getter
    @Setter
    public static class OptionValueRequest {
        @NotBlank
        @Size(max = 255)
        private String value;
    }
}
