package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.document.ProfileDocument;
import com.amsidh.mvc.repository.ProfileDocumentRepository;
import com.amsidh.mvc.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileDocumentRepository profileDocumentRepository;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public ProfileDocument createProfileDocument(ProfileDocument profileDocument) {
        UUID uuid = UUID.randomUUID();
        profileDocument.setId(uuid.toString());
        log.info("Going to save ProfileDocument {}", profileDocument);
        return profileDocumentRepository.save(profileDocument);
    }

    @Override
    public ProfileDocument findById(String id) {
        log.info("Find profile document with id {}", id);
        return profileDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Profile with id %s not found", id)));
    }

    @Override
    public ProfileDocument updateProfile(ProfileDocument profileDocument) {
        ProfileDocument existingProfileDocument = findById(profileDocument.getId());
        log.info("Updating profile document with id {}", profileDocument.getId());
        if (existingProfileDocument != null) {
            return profileDocumentRepository.save(profileDocument);
        } else {
            log.error("Profile document is not updated");
            return null;
        }
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
