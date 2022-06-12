package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionModel_;
import com.wifosell.zeus.model.user.User_;
import com.wifosell.zeus.repository.OptionRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.OptionService;
import com.wifosell.zeus.specs.OptionSpecs;
import com.wifosell.zeus.utils.ZeusUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service("OptionService")
@Transactional
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public Page<OptionModel> getOptions(Long userId, List<Boolean> isActives, Integer offset, Integer limit, String sortBy, String orderBy) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return optionRepository.findAll(
                OptionSpecs.hasGeneralManager(gmId)
                        .and(OptionSpecs.inIsActives(isActives)),
                ZeusUtils.getDefaultPageable(offset, limit, sortBy, orderBy)
        );
    }

    @Override
    public List<OptionModel> searchOptions(Long userId, String keyword, List<Boolean> isActives, Integer offset, Integer limit) {
        SearchSession searchSession = Search.session(entityManager);

        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        if (offset == null) {
            offset = 0;
        }
        if (limit == null || limit > 100) {
            limit = 100;
        }

        return searchSession.search(OptionModel.class).where(f -> f.bool(b -> {
            b.must(f.matchAll());
            if (gmId != null) {
                b.must(f.match().field(OptionModel_.GENERAL_MANAGER + "." + User_.ID).matching(gmId));
            }
            if (isActives == null || isActives.isEmpty()) {
                b.must(f.match().field(OptionModel_.IS_ACTIVE).matching(true));
            } else {
                b.must(f.terms().field(OptionModel_.IS_ACTIVE).matchingAny(isActives));
            }
            if (keyword != null && !keyword.isEmpty()) {
                b.must(f.match().field(OptionModel_.NAME).matching(keyword));
            }
        })).fetchHits(offset * limit, limit);
    }
}
