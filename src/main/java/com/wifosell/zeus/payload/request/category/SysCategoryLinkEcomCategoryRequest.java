package com.wifosell.zeus.payload.request.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SysCategoryLinkEcomCategoryRequest {

    List<LinkItem> linkCategories;

    @Getter
    public static class LinkItem {
        private Long sysCategoryId;
        private Long sendoCategoryId;
        private Long lazadaCategoryId;

        public LinkItem(Long sysCategoryId, Long sendoCategoryId, Long lazadaCategoryId) {
            this.sysCategoryId = sysCategoryId;
            this.sendoCategoryId = sendoCategoryId;
            this.lazadaCategoryId = lazadaCategoryId;
        }
    }
}
