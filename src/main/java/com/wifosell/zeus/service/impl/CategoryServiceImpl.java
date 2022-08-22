package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.constant.exception.EAppExceptionCode;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.GApiErrorBody;
import com.wifosell.zeus.payload.request.category.CategoryRequest;
import com.wifosell.zeus.payload.request.category.SysCategoryLinkEcomCategoryRequest;
import com.wifosell.zeus.payload.response.category.SysCategoryLinkEcomCategoryResponse;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryAndSysCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.LazadaCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.SendoCategoryAndSysCategoryRepository;
import com.wifosell.zeus.repository.ecom_sync.SendoCategoryRepository;
import com.wifosell.zeus.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("CategoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final SendoCategoryRepository sendoCategoryRepository;
    @Autowired
    private final LazadaCategoryRepository lazadaCategoryRepository;
    @Autowired
    private final SendoCategoryAndSysCategoryRepository sendoCategoryAndSysCategoryRepository;
    @Autowired
    private final LazadaCategoryAndSysCategoryRepository lazadaCategoryAndSysCategoryRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<Category> getAllRootCategories(Boolean isActive) {
        if (isActive == null)
            isActive = true;
        return categoryRepository.findAllRootsWithActive(isActive);
    }

    @Override
    public List<Category> getRootCategories(Long userId, Boolean isActive) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        if (isActive == null)
            isActive = true;
        return categoryRepository.findAllRootsWithGmAndActive(gm.getId(), isActive);
    }

    @Override
    public Category getCategory(Long userId, Long categoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        return categoryRepository.getByIdWithGm(gm.getId(), categoryId);
    }

    @Override
    public Category addCategory(Long userId, CategoryRequest categoryRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = new Category();
        return this.updateCategoryByRequest(category, categoryRequest, gm);
    }

    @Override
    public Category updateCategory(Long userId, Long categoryId, CategoryRequest categoryRequest) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = this.getCategory(userId, categoryId);
        return this.updateCategoryByRequest(category, categoryRequest, gm);
    }

    @Override
    public Category activateCategory(Long userId, Long categoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = categoryRepository.getByIdWithGm(gm.getId(), categoryId);
        category.setIsActive(true);
        return categoryRepository.save(category);
    }

    @Override
    public Category deactivateCategory(Long userId, Long categoryId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();
        Category category = categoryRepository.getByIdWithGm(gm.getId(), categoryId);
        category.setIsActive(false);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> activateCategories(Long userId, List<Long> categoryIds) {
        return categoryIds.stream().map(id -> this.activateCategory(userId, id)).collect(Collectors.toList());
    }

    @Override
    public List<Category> deactivateCategories(Long userId, List<Long> categoryIds) {
        return categoryIds.stream().map(id -> this.deactivateCategory(userId, id)).collect(Collectors.toList());
    }

    private Category updateCategoryByRequest(Category category, CategoryRequest categoryRequest, User gm) {
        Optional.ofNullable(categoryRequest.getName()).ifPresent(category::setName);
        Optional.ofNullable(categoryRequest.getDescription()).ifPresent(category::setDescription);
        Optional.ofNullable(categoryRequest.getShortName()).ifPresent(category::setShortName);
        Optional.ofNullable(categoryRequest.getParentCategoryId()).ifPresent(parentCategoryId -> {
            Category parentCategory = categoryRepository.findByIdWithGmAndActive(gm.getId(), parentCategoryId, true).orElseThrow(
                    () -> new AppException(GApiErrorBody.makeErrorBody(EAppExceptionCode.PARENT_CATEGORY_NOT_FOUND))
            );
            category.setParent(parentCategory);
        });
        Optional.ofNullable(categoryRequest.getIsActive()).ifPresent(category::setIsActive);
        category.setGeneralManager(gm);
        return categoryRepository.save(category);
    }
    //region Gợi ý thêm các category lazada
    private List<LazadaCategory> suggestUnlinkLazadaCategory(Long userId){
        List<LazadaCategory> listUnlinkLazadaCategories = new ArrayList<>();


        return listUnlinkLazadaCategories;
    }

    private SendoCategoryAndSysCategory linkSysCategorySendoCategory(Category sysCate, SendoCategory sendoCate) {
        try {
            //sendoCategoryAndSysCategoryRepository.deleteRelationWithSysCategoryIdAndNotEqualEcomId(sysCate.getId(), sendoCate.getId());
            SendoCategoryAndSysCategory sendoCategoryAndSysCategory = sendoCategoryAndSysCategoryRepository.findFirstBySendoCategoryId(sendoCate.getId()).orElse(null);
            if (sendoCategoryAndSysCategory == null) {
                sendoCategoryAndSysCategory = SendoCategoryAndSysCategory.builder()
                        .sendoCategory(sendoCate).sysCategory(sysCate).generalManager(sysCate.getGeneralManager()).build();
            } else {
                sendoCategoryAndSysCategory.setSysCategory(sysCate);
            }
            return sendoCategoryAndSysCategoryRepository.save(sendoCategoryAndSysCategory);
        } catch (Exception exception) {
            return null;
        }
    }

    private LazadaCategoryAndSysCategory linkSysCategoryLazadaCategory(Category sysCate, LazadaCategory lazadaCate) {
        try {
            //lazadaCategoryAndSysCategoryRepository.deleteRelationWithSysCategoryIdAndNotEqualEcomId(sysCate.getId(), lazadaCate.getId());
            LazadaCategoryAndSysCategory sendoCategoryAndSysCategory = lazadaCategoryAndSysCategoryRepository.findFirstByLazadaCategory(lazadaCate.getId()).orElse(null);
            if (sendoCategoryAndSysCategory == null) {
                sendoCategoryAndSysCategory = LazadaCategoryAndSysCategory.builder()
                        .lazadaCategory(lazadaCate)
                        .sysCategory(sysCate)
                        .generalManager(sysCate.getGeneralManager())
                        .build();
            } else {
                sendoCategoryAndSysCategory.setSysCategory(sysCate);
            }
            return lazadaCategoryAndSysCategoryRepository.save(sendoCategoryAndSysCategory);
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public SysCategoryLinkEcomCategoryResponse getAllLinkCategoryEcomCategory(Long userId) {
        User gm = userRepository.getUserById(userId).getGeneralManager();

        List<Category> categories = categoryRepository.getAllCategoryByUserId(gm.getId());
        SysCategoryLinkEcomCategoryResponse sysCategoryLinkEcomCategoryResponse = new SysCategoryLinkEcomCategoryResponse();
        List<SysCategoryLinkEcomCategoryResponse.LinkItemResponse> linkItemResponseList = new ArrayList<>();
        for (Category cate : categories) {
            SendoCategoryAndSysCategory _sendoCateLink = sendoCategoryAndSysCategoryRepository.findFirstBySysCategory(cate.getId()).orElse(null);
            LazadaCategoryAndSysCategory _lazadaCateLink = lazadaCategoryAndSysCategoryRepository.findFirstBySysCategory(cate.getId()).orElse(null);

            SysCategoryLinkEcomCategoryResponse.LinkItemResponse _linkItemResponse = new SysCategoryLinkEcomCategoryResponse.LinkItemResponse();
            _linkItemResponse.setSysCategoryId(cate.getId());

            SendoCategory _sendoCate = null;
            LazadaCategory _lazadaCate = null;

            if (_sendoCateLink != null) {
                _sendoCate = _sendoCateLink.getSendoCategory();
                _linkItemResponse.setSendoCategoryId(_sendoCate.getId());

            }
            if (_lazadaCateLink != null) {
                _lazadaCate = _lazadaCateLink.getLazadaCategory();
                _linkItemResponse.setSendoCategoryId(_lazadaCate.getId());
            }

            linkItemResponseList.add(_linkItemResponse);
        }

        sysCategoryLinkEcomCategoryResponse.setLinkCategories(linkItemResponseList);
        return sysCategoryLinkEcomCategoryResponse;
    }


    @Override
    public void linkCategoryEcomCategory(Long userId, SysCategoryLinkEcomCategoryRequest linkEcomCategoryRequest) {
        List<SysCategoryLinkEcomCategoryRequest.LinkItem> listLinkCates = linkEcomCategoryRequest.getLinkCategories();

        User gm = userRepository.getUserById(userId).getGeneralManager();

        Integer successLazadaLinked = 0;
        Integer successSendoLinked = 0;
        Integer failLazadaLinked = 0;
        Integer failSendoLinked = 0;

        //xoá toàn bộ bảng category = systemCategory và ecomCateg không trong list

        //
        for (var item : listLinkCates) {
            Category _sysCate = categoryRepository.getByIdWithGm(gm.getId(), item.getSysCategoryId());
            if (_sysCate == null) {
                continue;
            }
            SendoCategory _sendoCate = sendoCategoryRepository.getFirstById(item.getSendoCategoryId()).orElse(null);
            LazadaCategory _lazadaCate = lazadaCategoryRepository.getFirstById(item.getLazadaCategoryId()).orElse(null);


            if (_sendoCate != null) {
                SendoCategoryAndSysCategory _sendoLinked = this.linkSysCategorySendoCategory(_sysCate, _sendoCate);
                if (_sendoLinked == null) {
                    failSendoLinked += 1;
                } else {
                    successSendoLinked += 1;
                }
            }
            if (_lazadaCate != null) {
                LazadaCategoryAndSysCategory _lazadaLinked = this.linkSysCategoryLazadaCategory(_sysCate, _lazadaCate);
                if (_lazadaLinked == null) {
                    failLazadaLinked += 1;
                } else {
                    successLazadaLinked += 1;
                }
            }
        }
    }
}
