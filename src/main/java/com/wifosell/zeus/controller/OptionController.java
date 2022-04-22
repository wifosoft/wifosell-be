package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.response.option.OptionResponse;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.OptionService;
import com.wifosell.zeus.utils.Preprocessor;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<GApiResponse<List<OptionResponse>>> getAllOrders(
            @RequestParam(name = "active", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<OptionModel> options = optionService.getAllOptions(isActive);
        List<OptionResponse> responses = options.stream().map(OptionResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<OptionResponse>>> getOrders(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "active", required = false) List<Boolean> actives
    ) {
        Boolean isActive = Preprocessor.convertToIsActive(actives);
        List<OptionModel> options = optionService.getOptions(userPrincipal.getId(), isActive);
        List<OptionResponse> responses = options.stream().map(OptionResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(GApiResponse.success(responses));
    }
}
