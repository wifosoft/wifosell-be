package com.wifosell.zeus.service;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.payload.request.option.OptionRequest;

import java.util.List;

public interface OptionService {
    List<OptionModel> getAllOptions();
    List<OptionModel> getOptionsByUserId(Long userId);
    OptionModel getOption(Long optionId);
    OptionModel addOption(Long userId, OptionRequest optionRequest);
    OptionModel updateOption(Long optionId, OptionRequest optionRequest);
}
