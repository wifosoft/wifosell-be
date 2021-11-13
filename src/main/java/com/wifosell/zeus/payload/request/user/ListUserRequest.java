package com.wifosell.zeus.payload.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ListUserRequest{
    @NotNull
    List<Long> listUserId;
}
