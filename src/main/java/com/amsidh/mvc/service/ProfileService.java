package com.amsidh.mvc.service;

import com.amsidh.mvc.document.ProfileDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
    String createProfileDocument(ProfileDocument profileDocument) throws IOException;

    ProfileDocument findById(String id) throws IOException;

    String updateProfile(ProfileDocument profileDocument) throws IOException;

    List<ProfileDocument> findAllProfiles() throws IOException;

    List<ProfileDocument> findProfilesByTechnologyName(String technologyName) throws IOException;

    String deleteProfileById(String id) throws IOException;
}
