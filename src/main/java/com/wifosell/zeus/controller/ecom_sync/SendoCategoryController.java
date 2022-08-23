package com.wifosell.zeus.controller.ecom_sync;

import com.wifosell.zeus.model.ecom_sync.SendoCategory;
import com.wifosell.zeus.model.ecom_sync.SendoCategoryAndSysCategory;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkCategoryRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.SendoCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/link")
    public ResponseEntity<GApiResponse<SendoCategoryAndSysCategory>> linkWithSystemCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid SendoLinkCategoryRequest request
    ) {
        SendoCategoryAndSysCategory link = sendoCategoryService.linkWithSysCategory(
                userPrincipal.getId(), request.getSendoCategoryId(), request.getSysCategoryId());
        if (link == null) return ResponseEntity.ok(GApiResponse.fail(null));
        return ResponseEntity.ok(GApiResponse.success(link));
    }

    @GetMapping("/link")
    public ResponseEntity<GApiResponse<List<SendoCategoryAndSysCategory>>> getLinks(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        List<SendoCategoryAndSysCategory> links = sendoCategoryService.getLinks(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(links));
    }

    @GetMapping("/link/{sysCategoryId}")
    public ResponseEntity<GApiResponse<SendoCategoryAndSysCategory>> getLinkBySysCategory(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "sysCategoryId") Long sysCategoryId
    ) {
        SendoCategoryAndSysCategory link = sendoCategoryService.getLink(userPrincipal.getId(), sysCategoryId);
        return ResponseEntity.ok(GApiResponse.success(link));
    }
}
