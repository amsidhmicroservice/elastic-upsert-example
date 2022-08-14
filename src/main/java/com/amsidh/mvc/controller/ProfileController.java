package com.amsidh.mvc.controller;

import com.amsidh.mvc.document.ProfileDocument;
import com.amsidh.mvc.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ProfileDocument saveProfile(@RequestBody ProfileDocument profileDocument) {
        return profileService.createProfileDocument(profileDocument);
    }

    @GetMapping("/{id}")
    public ProfileDocument findById(@PathVariable String id) {
        return profileService.findById(id);
    }

    @PutMapping
    public ProfileDocument updateProfile(@RequestBody ProfileDocument profileDocument) {
        return profileService.updateProfile(profileDocument);
    }

    @GetMapping
    public List<ProfileDocument> findAllProfiles() {
        return profileService.findAllProfiles();
    }

    @GetMapping("/search")
    public List<ProfileDocument> findProfilesByTechnologyName(@RequestParam("technologyName") String technologyName) {

        return profileService.findProfilesByTechnologyName(technologyName);
    }

    @DeleteMapping("/{id}")
    public String deleteProfileById(@PathVariable String id) {
        profileService.deleteProfileById(id);
        return String.format("Profile with id %s is deleted successfully", id);
    }
}
