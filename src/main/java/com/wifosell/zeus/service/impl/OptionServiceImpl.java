package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.repository.OptionRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.OptionService;
import com.wifosell.zeus.specs.OptionSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("OptionService")
@Transactional
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    @Override
    public Page<OptionModel> getOptions(Long userId, List<Boolean> isActives, Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return optionRepository.findAll(
                OptionSpecs.hasGeneralManager(gmId)
                        .and(OptionSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }
}
