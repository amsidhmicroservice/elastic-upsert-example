package com.amsidh.mvc.service;

import com.amsidh.mvc.document.ProfileDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
    ProfileDocument createProfileDocument(ProfileDocument profileDocument);

    ProfileDocument findById(String id);

    ProfileDocument updateProfile(ProfileDocument profileDocument);

    List<ProfileDocument> findAllProfiles();

    List<ProfileDocument> findProfilesByTechnologyName(String technologyName);

    void deleteProfileById(String id);
}
