package com.amsidh.mvc.controller;

import com.amsidh.mvc.document.ProfileDocument;
import com.amsidh.mvc.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public String saveProfile(@RequestBody ProfileDocument profileDocument) throws IOException {
        return profileService.createProfileDocument(profileDocument);
    }

    @GetMapping("/{id}")
    public ProfileDocument findById(@PathVariable String id) throws IOException {
        return profileService.findById(id);
    }

    @PutMapping
    public String updateProfile(@RequestBody ProfileDocument profileDocument) throws IOException {
        return profileService.updateProfile(profileDocument);
    }

    @GetMapping
    public List<ProfileDocument> findAllProfiles() throws IOException {
        return profileService.findAllProfiles();
    }

    @GetMapping("/search")
    public List<ProfileDocument> findProfilesByTechnologyName(@RequestParam("technologyName") String technologyName) throws IOException {

        return profileService.findProfilesByTechnologyName(technologyName);
    }

    @DeleteMapping("/{id}")
    public String deleteProfileById(@PathVariable String id) throws IOException {
        return profileService.deleteProfileById(id);
    }
}
