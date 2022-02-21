package com.wifosell.zeus.payload.request.sale_channel;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class DeactivateSaleChannelsRequest {
    @NotEmpty
    List<Long> ids;
}
