package com.wifosell.zeus.service;

import com.wifosell.zeus.model.option.OptionModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OptionService {
    Page<OptionModel> getOptions(
            Long userId,
            List<Boolean> isActives,
            Integer offset,
            Integer limit,
            String sortBy,
            String orderBy
    );
}
