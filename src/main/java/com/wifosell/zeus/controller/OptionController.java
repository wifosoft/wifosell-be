package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.option.OptionRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/options")
public class OptionController {
    private final OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<OptionModel>>> getAllOptions() {
        List<OptionModel> options = optionService.getAllOptions();
        return ResponseEntity.ok(GApiResponse.success(options));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<OptionModel>>> getOptionsByUser(
            @CurrentUser UserPrincipal userPrincipal) {
        List<OptionModel> options = optionService.getOptionsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(options));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{optionId}")
    public ResponseEntity<GApiResponse<OptionModel>> getOption(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "optionId") Long optionId) {
        OptionModel option = optionService.getOption(optionId);
        return ResponseEntity.ok(GApiResponse.success(option));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<OptionModel>> addOption(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody OptionRequest optionRequest) {
        OptionModel option = optionService.addOption(userPrincipal.getId(), optionRequest);
        return ResponseEntity.ok(GApiResponse.success(option));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{optionId}/update")
    public ResponseEntity<GApiResponse<OptionModel>> updateOption(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "optionId") Long optionId,
            @RequestBody OptionRequest optionRequest) {
        OptionModel option = optionService.updateOption(optionId, optionRequest);
        return ResponseEntity.ok(GApiResponse.success(option));
    }
}
