package com.wifosell.zeus.payload.response;

import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public abstract class BasicEntityResponse {
    private final Long id;
    private final boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;

    public boolean getIsActive() {
        return isActive;
    }

    public BasicEntityResponse(BasicEntity entity) {
        this.id = entity.getId();
        this.isActive = entity.getIsActive();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
