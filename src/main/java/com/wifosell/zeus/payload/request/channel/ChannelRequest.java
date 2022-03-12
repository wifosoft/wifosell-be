package com.wifosell.zeus.payload.request.channel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChannelRequest {
    @NotBlank
    private String channelName;

    @Size(max = 50)
    @Column(name = "short_name")
    private String shortName;

    @Size(max = 255)
    private String description;
}
