package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.SearchRequest;
import com.wifosell.zeus.payload.response.option.OptionResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.OptionService;
import com.wifosell.zeus.utils.paging.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/options")
@AllArgsConstructor
public class OptionController {
    private final OptionService optionService;

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<Page<OptionResponse>>> getAllOptions(
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<OptionModel> options = optionService.getOptions(
                null, isActives, offset, limit, sortBy, orderBy);
        Page<OptionResponse> responses = options.map(OptionResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<Page<OptionResponse>>> getOptions(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "orderBy", required = false) String orderBy
    ) {
        Page<OptionModel> options = optionService.getOptions(
                userPrincipal.getId(), isActives, offset, limit, sortBy, orderBy);
        Page<OptionResponse> responses = options.map(OptionResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/search")
    public ResponseEntity<GApiResponse<PageInfo<OptionResponse>>> searchOptions(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid SearchRequest request,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        PageInfo<OptionModel> options = optionService.searchOptions(userPrincipal.getId(), request.getKeyword(), isActives, offset, limit);
        PageInfo<OptionResponse> responses = options.map(OptionResponse::new);
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
