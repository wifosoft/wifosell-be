package com.wifosell.zeus.payload.response;

import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BasicEntityResponse {
    private final boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;

    public BasicEntityResponse(BasicEntity entity) {
        this.isActive = entity.isActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
