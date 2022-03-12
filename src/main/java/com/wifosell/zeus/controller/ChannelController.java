package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.channel.Channel;
import com.wifosell.zeus.payload.GApiResponse;
import com.wifosell.zeus.payload.request.channel.ChannelRequest;
import com.wifosell.zeus.security.CurrentUser;
import com.wifosell.zeus.security.UserPrincipal;
import com.wifosell.zeus.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/channels")
public class ChannelController {
    private final ChannelService channelService;

    @Autowired
    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<GApiResponse<List<Channel>>> getAllChannels() {
        List<Channel> channelList = channelService.getAllChannels();
        return ResponseEntity.ok(GApiResponse.success(channelList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<GApiResponse<List<Channel>>> getChannels(@CurrentUser UserPrincipal userPrincipal) {
        List<Channel> channelList = channelService.getChannelsByUserId(userPrincipal.getId());
        return ResponseEntity.ok(GApiResponse.success(channelList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/shop={shopId}")
    public ResponseEntity<GApiResponse<List<Channel>>> getChannelsByShopId(@CurrentUser UserPrincipal userPrincipal,
                                                                                   @PathVariable(name = "shopId") Long shopId) {
        List<Channel> channelList = channelService.getChannelsByShopId(shopId);
        return ResponseEntity.ok(GApiResponse.success(channelList));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{channelId}")
    public ResponseEntity<GApiResponse<Channel>> getChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                    @PathVariable(name = "channelId") Long channelId) {
        Channel channel = channelService.getChannel(channelId);
        return ResponseEntity.ok(GApiResponse.success(channel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("")
    public ResponseEntity<GApiResponse<Channel>> addChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                    @RequestBody ChannelRequest channelRequest) {
        Channel channel = channelService.addChannel(userPrincipal.getId(), channelRequest);
        return ResponseEntity.ok(GApiResponse.success(channel));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{channelId}/update")
    public ResponseEntity<GApiResponse<Channel>> addChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                    @PathVariable(name = "channelId") Long channelId,
                                                                    @RequestBody ChannelRequest channelRequest) {
        Channel channel = channelService.updateChannel(channelId, channelRequest);
        return ResponseEntity.ok(GApiResponse.success(channel));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{channelId}/activate")
    public ResponseEntity<GApiResponse<Channel>> activateChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                         @PathVariable(name = "channelId") Long channelId) {
        Channel channel = channelService.activateChannel(channelId);
        return ResponseEntity.ok(GApiResponse.success(channel));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{channelId}/deactivate")
    public ResponseEntity<GApiResponse<Channel>> deactivateChannel(@CurrentUser UserPrincipal userPrincipal,
                                                                           @PathVariable(name = "channelId") Long channelId) {
        Channel channel = channelService.deactivateChannel(channelId);
        return ResponseEntity.ok(GApiResponse.success(channel));
    }
}
