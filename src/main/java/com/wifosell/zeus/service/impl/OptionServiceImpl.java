package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.OptionRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.OptionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
    public List<OptionModel> getAllOptions(Boolean isActive) {
        if (isActive == null)
            return optionRepository.findAll();
        return optionRepository.findAllWithActive(isActive);
    }

    @Override
    public List<OptionModel> getOptions(@NonNull Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            return optionRepository.findAllWithGm(gm.getId());
        return optionRepository.findAllWithGmAndActive(gm.getId(), isActive);
    }
}
