package com.wifosell.zeus.service;

import com.wifosell.zeus.model.option.OptionModel;
import lombok.NonNull;

import java.util.List;

public interface OptionService {
    List<OptionModel> getAllOptions(Boolean isActive);

    List<OptionModel> getOptions(@NonNull Long userId, Boolean isActive);
}
