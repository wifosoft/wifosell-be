package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.pricetrack.PriceTrack;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.common.ListIdRequest;
import com.wifosell.zeus.payload.request.pricetrack.AddPriceTrackRequest;
import com.wifosell.zeus.payload.request.pricetrack.UpdatePriceTrackRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.PriceTrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/price_tracks")
public class PriceTrackController {
    private final PriceTrackService priceTrackService;

    @GetMapping("/test/trigger")
    public ResponseEntity<GApiResponse<Boolean>> testTrigger(
            @RequestParam(name = "priceTrackId") Long priceTrackId,
            @RequestParam(name = "newCompetitorPrice") BigDecimal newCompetitorPrice
    ) {
        priceTrackService.trigger(priceTrackId, newCompetitorPrice);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    @GetMapping("/test/get_map")
    public ResponseEntity<GApiResponse<Map<Long, String>>> testGetMapActiveCompetitorUrls() {
        Map<Long, String> map = priceTrackService.getMapActiveCompetitorUrls();
        return ResponseEntity.ok(GApiResponse.success(map));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<PriceTrack>>> getPriceTracks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "isActive", required = false) List<Boolean> isActives
    ) {
        List<PriceTrack> priceTracks = priceTrackService.getPriceTracks(userPrincipal.getId(), isActives);
        return ResponseEntity.ok(GApiResponse.success(priceTracks));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{priceTrackId}")
    public ResponseEntity<GApiResponse<PriceTrack>> getPriceTrack(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "priceTrackId") Long priceTrackId
    ) {
        PriceTrack priceTrack = priceTrackService.getPriceTrack(userPrincipal.getId(), priceTrackId);
        return ResponseEntity.ok(GApiResponse.success(priceTrack));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<PriceTrack>> addPriceTrack(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid AddPriceTrackRequest request
    ) {
        PriceTrack priceTrack = priceTrackService.addPriceTrack(userPrincipal.getId(), request);
        return ResponseEntity.ok(GApiResponse.success(priceTrack));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{priceTrackId}/update")
    public ResponseEntity<GApiResponse<PriceTrack>> updatePriceTrack(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "priceTrackId") Long priceTrackId,
            @RequestBody @Valid UpdatePriceTrackRequest request
    ) {
        PriceTrack priceTrack = priceTrackService.updatePriceTrack(userPrincipal.getId(), priceTrackId, request);
        return ResponseEntity.ok(GApiResponse.success(priceTrack));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{priceTrackId}/delete")
    public ResponseEntity<GApiResponse<Boolean>> deletePriceTrack(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(name = "priceTrackId") Long priceTrackId
    ) {
        priceTrackService.deletePriceTrack(userPrincipal.getId(), priceTrackId);
        return ResponseEntity.ok(GApiResponse.success(true));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deactivate")
    public ResponseEntity<GApiResponse<List<PriceTrack>>> deactivatePriceTracks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<PriceTrack> priceTracks = priceTrackService.deactivatePriceTracks(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(priceTracks));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/activate")
    public ResponseEntity<GApiResponse<List<PriceTrack>>> activatePriceTracks(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody @Valid ListIdRequest request
    ) {
        List<PriceTrack> priceTracks = priceTrackService.activatePriceTracks(userPrincipal.getId(), request.getIds());
        return ResponseEntity.ok(GApiResponse.success(priceTracks));
    }
}
