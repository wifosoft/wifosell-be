package com.wifosell.zeus.payload.response;

import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BasicEntityResponse {
    private final Long id;
    private final boolean isActive;

    public BasicEntityResponse(BasicEntity entity) {
        this.id = entity.getId();
        this.isActive = entity.isActive();
    }
}
