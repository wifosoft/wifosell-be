package com.wifosell.zeus.controller;

import com.wifosell.zeus.payload.GApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GeneralController {
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('GLOBAL_ACCESS')")
    @RequestMapping("/g")
    public ResponseEntity<GApiResponse> index() {
        return ResponseEntity.ok(new GApiResponse(Boolean.TRUE, "Global API here!!! Zeus is coming Add shop"));
    }
    
    @RequestMapping("/g/version")
    public ResponseEntity<GApiResponse> version() {
        return ResponseEntity.ok(GApiResponse.success("Version 1.0.0 - test deploy"));
    }
    
}

