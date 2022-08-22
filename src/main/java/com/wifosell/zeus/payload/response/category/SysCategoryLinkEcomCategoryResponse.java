package com.wifosell.zeus.payload.response.category;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import com.wifosell.zeus.payload.request.category.SysCategoryLinkEcomCategoryRequest;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SysCategoryLinkEcomCategoryResponse {
    List<LinkItemResponse> linkCategories;

    @Getter
    @Setter
    public static class LinkItemResponse {
        private Long sysCategoryId;
        private Long sendoCategoryId;
        private Long lazadaCategoryId;

        public LinkItemResponse() {

        }
        public LinkItemResponse(Long sysCategoryId, Long sendoCategoryId, Long lazadaCategoryId) {
            this.sysCategoryId = sysCategoryId;
            this.sendoCategoryId = sendoCategoryId;
            this.lazadaCategoryId = lazadaCategoryId;
        }
    }

}
