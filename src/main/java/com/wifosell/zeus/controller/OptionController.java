package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.response.option.OptionResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.OptionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/search")
    public ResponseEntity<GApiResponse<List<OptionResponse>>> searchOptions(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        List<OptionModel> options = optionService.searchOptions(keyword, userPrincipal.getId(), isActives, offset, limit);
        List<OptionResponse> responses = options.stream().map(OptionResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
