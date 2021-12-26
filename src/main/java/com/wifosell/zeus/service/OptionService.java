package com.wifosell.zeus.service;

import com.wifosell.zeus.model.product.Option;
import com.wifosell.zeus.payload.request.product.OptionRequest;

import java.util.List;

public interface OptionService {
    List<Option> getAllOptions();
    List<Option> getOptionsByUserId(Long userId);
    Option getOption(Long optionId);
    Option addOption(Long userId, OptionRequest optionRequest);
    Option updateOption(Long optionId, OptionRequest optionRequest);
}
