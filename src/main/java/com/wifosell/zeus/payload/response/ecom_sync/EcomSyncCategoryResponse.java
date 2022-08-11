package com.wifosell.zeus.payload.response.ecom_sync;

import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EcomSyncCategoryResponse {
    private Category sysCategory;
    private LazadaCategory lazadaCategory;
    private SendoCategory sendoCategory;
}
