package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.LazadaCategory;
import com.wifosell.zeus.model.ecom_sync.LazadaCategoryAndSysCategory;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.ecom_sync.LazadaLinkCategoryRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.LazadaCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/link")
    public ResponseEntity<GApiResponse<LazadaCategoryAndSysCategory>> linkWithSystemCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid LazadaLinkCategoryRequest request
    ) {
        LazadaCategoryAndSysCategory link = lazadaCategoryService.linkWithSysCategory(
                userPrincipal.getId(), request.getLazadaCategoryId(), request.getSysCategoryId());
        if (link == null) return ResponseEntity.ok(GApiResponse.fail(null));
        return ResponseEntity.ok(GApiResponse.success(link));
    }

    @GetMapping("/link")
    public ResponseEntity<GApiResponse<List<LazadaCategoryAndSysCategory>>> getLinks(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        List<LazadaCategoryAndSysCategory> links = lazadaCategoryService.getLinks(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(links));
    }

    @GetMapping("/link/{sysCategoryId}")
    public ResponseEntity<GApiResponse<LazadaCategoryAndSysCategory>> getLinkBySysCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "sysCategoryId") Long sysCategoryId
    ) {
        LazadaCategoryAndSysCategory link = lazadaCategoryService.getLink(userPrincipal.getId(), sysCategoryId);
        return ResponseEntity.ok(GApiResponse.success(link));
    }
}
