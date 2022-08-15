package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.document.ProfileDocument;
import com.amsidh.mvc.repository.ProfileDocumentRepository;
import com.amsidh.mvc.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileDocumentRepository profileDocumentRepository;

    @Override
    public ProfileDocument createProfileDocument(ProfileDocument profileDocument) {
        UUID uuid = UUID.randomUUID();
        profileDocument.setId(uuid.toString());
        return profileDocumentRepository.save(profileDocument);
    }

    @Override
    public ProfileDocument findById(String id) {
        return profileDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Profile with id %s not found", id)));
    }

    @Override
    public ProfileDocument updateProfile(ProfileDocument profileDocument) {
        ProfileDocument existingProfileDocument = findById(profileDocument.getId());

        Optional.ofNullable(existingProfileDocument).ifPresent(profile -> {
            profileDocumentRepository.save(profileDocument);
        });
        return findById(profileDocument.getId());
    }

    @Override
    public List<ProfileDocument> findAllProfiles() {
        return StreamSupport.stream(profileDocumentRepository.findAll().spliterator(), true).collect(Collectors.toList());
    }

    @Override
    public List<ProfileDocument> findProfilesByTechnologyName(String technologyName) {
        return profileDocumentRepository.findByTechnologiesName(technologyName);
    }

    @Override
    public void deleteProfileById(String id) {
        profileDocumentRepository.deleteById(id);
    }

}
