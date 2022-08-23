package com.wifosell.zeus.payload.response;

import com.wifosell.zeus.model.audit.BasicEntity;
import lombok.Getter;

@Getter
public abstract class BasicEntityResponse {
    private final Long id;
    private final boolean isActive;
    private final boolean isDeleted;
    private final Long createdAt;
    private final Long updatedAt;

    public boolean getIsActive() {
        return isActive;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public BasicEntityResponse(BasicEntity entity) {

        this.id = entity.getId();
        this.isActive = entity.getIsActive();
        this.isDeleted = entity.isDeleted();
        this.createdAt = entity.getCreatedAt().toEpochMilli();
        this.updatedAt = entity.getUpdatedAt().toEpochMilli();
    }
}
