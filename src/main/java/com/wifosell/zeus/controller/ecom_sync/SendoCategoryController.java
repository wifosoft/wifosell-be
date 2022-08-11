package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.service.SendoCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/ecom_sync/sendo/category")
public class SendoCategoryController {
    private final SendoCategoryService sendoCategoryService;

    @GetMapping("/leaf")
    public ResponseEntity<GApiResponse<List<SendoCategory>>> getLeafCategories() {
        List<SendoCategory> categories = sendoCategoryService.getLeafCategories();
        return ResponseEntity.ok(GApiResponse.success(categories));
    }

    @GetMapping("/root")
    public ResponseEntity<GApiResponse<List<SendoCategory>>> getRootCategories() {
        List<SendoCategory> categories = sendoCategoryService.getRootCategories();
        return ResponseEntity.ok(GApiResponse.success(categories));
    }
}
