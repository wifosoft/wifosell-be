package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.option.Option;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.option.OptionRequest;
import com.wifosell.zeus.repository.OptionRepository;
import com.wifosell.zeus.repository.OptionValueRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.OptionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service("Option")
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final OptionValueRepository optionValueRepository;
    private final UserRepository userRepository;

    public OptionServiceImpl(OptionRepository optionRepository,
                             OptionValueRepository optionValueRepository, UserRepository userRepository) {
        this.optionRepository = optionRepository;
        this.optionValueRepository = optionValueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    @Override
    public List<Option> getOptionsByUserId(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return optionRepository.findOptionsByGeneralManagerId(gm.getId());
    }

    @Override
    public Option getOption(Long optionId) {
        return optionRepository.findOptionById(optionId);
    }

    @Override
    public Option addOption(Long userId, OptionRequest optionRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Option option = new Option();
        this.updateOptionByRequest(option, optionRequest);
        option.setGeneralManager(gm);
        return optionRepository.save(option);
    }

    @Override
    public Option updateOption(Long optionId, OptionRequest optionRequest) {
        Option option = optionRepository.findOptionById(optionId);
        this.updateOptionByRequest(option, optionRequest);
        return optionRepository.save(option);
    }

    private void updateOptionByRequest(Option option, OptionRequest optionRequest) {
        Optional.ofNullable(optionRequest.getName()).ifPresent(option::setName);
        Optional.ofNullable(optionRequest.getOptionValueRequests()).ifPresent(optionValueRequests -> {
            // TODO haukc: optimize performance
            optionValueRepository.deleteOptionValuesByOptionId(option.getId());

            List<OptionValue> optionValues = new ArrayList<>();
            for (OptionRequest.OptionValueRequest optionValueRequest : optionValueRequests) {
                OptionValue optionValue = OptionValue.builder()
                        .value(optionValueRequest.getValue())
                        .option(option)
                        .build();
                optionValues.add(optionValueRepository.save(optionValue));
            }
            option.setOptionValues(optionValues);
            optionRepository.save(option);
        });
        Optional.ofNullable(optionRequest.getActive()).ifPresent(option::setIsActive);
    }
}
