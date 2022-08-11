package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.service.LazadaCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ecom_sync/lazada/category")
public class LazadaCategoryController {
    private final LazadaCategoryService lazadaCategoryService;

    @GetMapping("/leaf")
    public ResponseEntity<GApiResponse<List<LazadaCategory>>> getLeafCategories() {
        List<LazadaCategory> categories = lazadaCategoryService.getLeafCategories();
        return ResponseEntity.ok(GApiResponse.success(categories));
    }

    @GetMapping("/root")
    public ResponseEntity<GApiResponse<List<LazadaCategory>>> getRootCategories() {
        List<LazadaCategory> categories = lazadaCategoryService.getRootCategories();
        return ResponseEntity.ok(GApiResponse.success(categories));
    }
}
