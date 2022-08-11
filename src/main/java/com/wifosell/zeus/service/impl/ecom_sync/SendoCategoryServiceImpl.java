package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.SendoCategoryAndSysCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.SendoCategoryRepository;
import com.wifosell.zeus.service.CategoryService;
import com.wifosell.zeus.service.SendoCategoryService;
import com.wifosell.zeus.specs.SendoCategoryAndSysCategorySpecs;
import com.wifosell.zeus.specs.SendoCategorySpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service("SendoCategoryService")
@Transactional
@RequiredArgsConstructor
public class SendoCategoryServiceImpl implements SendoCategoryService {
    private final UserRepository userRepository;
    private final SendoCategoryRepository sendoCategoryRepository;
    private final SendoCategoryAndSysCategoryRepository sendoCategoryAndSysCategoryRepository;
    private final CategoryService categoryService;

    @Override
    public List<SendoCategory> getLeafCategories() {
        return sendoCategoryRepository.findAll(
                SendoCategorySpecs.isLeaf()
        );
    }

    @Override
    public List<SendoCategory> getRootCategories() {
        return sendoCategoryRepository.findAll(
                SendoCategorySpecs.isRoot()
        );
    }

    @Override
    public SendoCategoryAndSysCategory linkWithSysCategory(Long userId, Long sendoCategoryId, Long sysCategoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();

        SendoCategoryAndSysCategory link = sendoCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(gm.getId(), sysCategoryId).orElse(null);

        if (link != null && sendoCategoryId == null) {
            link.setSendoCategory(null);
            sendoCategoryAndSysCategoryRepository.delete(link);
            return link;
        }

        SendoCategory sendoCategory = sendoCategoryRepository.getById(sendoCategoryId);

        if (!sendoCategory.isLeaf()) {
            throw new ZeusGlobalException(HttpStatus.OK, "Sendo category cần là category cấp cuối cùng.");
        }

        if (link != null) {
            link.setSendoCategory(sendoCategory);
        } else {
            Category sysCategory = categoryService.getCategory(userId, sysCategoryId);

            link = SendoCategoryAndSysCategory.builder()
                    .sysCategory(sysCategory)
                    .sendoCategory(sendoCategory)
                    .generalManager(gm)
                    .build();
        }

        return sendoCategoryAndSysCategoryRepository.save(link);
    }

    @Override
    public List<SendoCategoryAndSysCategory> getLinks(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return sendoCategoryAndSysCategoryRepository.findAll(
                SendoCategoryAndSysCategorySpecs.hasGeneralManagerId(gmId)
        );
    }

    @Override
    public SendoCategoryAndSysCategory getLink(Long userId, Long sysCategoryId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return sendoCategoryAndSysCategoryRepository.getOne(
                SendoCategoryAndSysCategorySpecs.hasGeneralManagerId(gmId)
                        .and(SendoCategoryAndSysCategorySpecs.hasSysCategoryId(sysCategoryId))
        );
    }

    @Override
    public Optional<SendoCategoryAndSysCategory> findLink(Long userId, Long sysCategoryId) {
        return sendoCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(userId, sysCategoryId);
    }
}
