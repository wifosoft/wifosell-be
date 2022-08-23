package com.wifosell.zeus.service.impl.ecom_sync;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaProduct;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAndSysCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaProductRepository;
import com.wifosell.zeus.service.CategoryService;
import com.wifosell.zeus.service.LazadaCategoryService;
import com.wifosell.zeus.specs.LazadaCategoryAndSysCategorySpecs;
import com.wifosell.zeus.specs.LazadaCategorySpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("LazadaCategoryService")
@Transactional
@RequiredArgsConstructor
public class LazadaCategoryServiceImpl implements LazadaCategoryService {
    private final UserRepository userRepository;
    private final LazadaCategoryRepository lazadaCategoryRepository;
    private final LazadaCategoryAndSysCategoryRepository lazadaCategoryAndSysCategoryRepository;
    private final CategoryService categoryService;

    @Autowired
    LazadaProductRepository lazadaProductRepository;

    @Override
    public List<LazadaCategory> getLeafCategories() {
        return lazadaCategoryRepository.findAll(
                LazadaCategorySpecs.isLeaf()
        );
    }

    @Override
    public List<LazadaCategory> getRootCategories() {
        return lazadaCategoryRepository.findAll(
                LazadaCategorySpecs.isRoot()
        );
    }

    @Override
    public LazadaCategoryAndSysCategory linkWithSysCategory(Long userId, Long lazadaCategoryId, Long sysCategoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();

        LazadaCategoryAndSysCategory link = lazadaCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(gm.getId(), sysCategoryId).orElse(null);

        if (lazadaCategoryId == null) {
            if (link != null) {
                link.setLazadaCategory(null);
                lazadaCategoryAndSysCategoryRepository.delete(link);
            }
            return link;
        }

        LazadaCategory lazadaCategory = lazadaCategoryRepository.getById(lazadaCategoryId);

        if (!lazadaCategory.isLeaf()) {
            throw new ZeusGlobalException(HttpStatus.OK, "Lazada category cần là category cấp cuối cùng.");
        }

        if (link != null) {
            link.setLazadaCategory(lazadaCategory);
        } else {
            Category sysCategory = categoryService.getCategory(userId, sysCategoryId);

            link = LazadaCategoryAndSysCategory.builder()
                    .sysCategory(sysCategory)
                    .lazadaCategory(lazadaCategory)
                    .generalManager(gm)
                    .build();
        }

        return lazadaCategoryAndSysCategoryRepository.save(link);
    }

    @Override
    public List<LazadaCategoryAndSysCategory> getLinks(Long userId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaCategoryAndSysCategoryRepository.findAll(
                LazadaCategoryAndSysCategorySpecs.hasGeneralManagerId(gmId)
        );
    }

    @Override
    public LazadaCategoryAndSysCategory getLink(Long userId, Long sysCategoryId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaCategoryAndSysCategoryRepository.getOne(
                LazadaCategoryAndSysCategorySpecs.hasGeneralManagerId(gmId)
                        .and(LazadaCategoryAndSysCategorySpecs.hasSysCategoryId(sysCategoryId))
        );
    }

    @Override
    public Optional<LazadaCategoryAndSysCategory> findLink(Long userId, Long sysCategoryId) {
        Long gmId = userId == null ? null : userRepository.getUserById(userId).getGeneralManager().getId();
        return lazadaCategoryAndSysCategoryRepository.findByGeneralManagerIdAndSysCategoryId(gmId, sysCategoryId);
    }

    @Override
    public List<LazadaCategory> getUnlinkLazadaCategory(Long userId) {
        List<LazadaCategory> unlinkCates = new ArrayList<>();
        User gm = userRepository.findById(userId).orElseThrow(() -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.USER_NOT_FOUND))).getGeneralManager();

        List<LazadaCategoryAndSysCategory> listLinkeLazadaCate = this.getLinks(userId);

        List<Long> aggregateLonLazadaLinkedId = listLinkeLazadaCate.stream()
                .map(LazadaCategoryAndSysCategory::getLazadaCategory)
                .map(LazadaCategory::getId)
                .distinct()
                .collect(Collectors.toList());


        List<LazadaProduct> listSendoProduct = lazadaProductRepository.getListLazadaProductByGm(gm.getId());
        List<Long> aggregateLongSendoProductPrimaryId = listSendoProduct.stream().map(LazadaProduct::getPrimaryCategory).collect(Collectors.toList()); //id product existed

        List<LazadaCategory> listLazadaCategories = lazadaCategoryRepository.getLazadaCategoryByListLazadaCateId(aggregateLongSendoProductPrimaryId);

        //category tồn tại nhưng chưa được liên kết, not in aggresSEndoLinkeđI
        unlinkCates = listLazadaCategories.stream().filter(sendoCate ->
                !aggregateLonLazadaLinkedId.contains(sendoCate.getId())).collect(Collectors.toList());

        return unlinkCates;
    }
}
