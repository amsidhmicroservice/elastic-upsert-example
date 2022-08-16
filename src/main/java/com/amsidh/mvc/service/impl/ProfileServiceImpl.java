package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.document.ProfileDocument;
import com.amsidh.mvc.repository.ProfileDocumentRepository;
import com.amsidh.mvc.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileDocumentRepository profileDocumentRepository;

    private static final String PROFILE_INDEX = "profile";

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public ProfileDocument createProfileDocument(ProfileDocument profileDocument) {
        UUID uuid = UUID.randomUUID();
        profileDocument.setId(uuid.toString());
        //return profileDocumentRepository.save(profileDocument);*/

        //Save ProfileDocument
        ProfileDocument savedProfileDocument = elasticsearchOperations.save(profileDocument, IndexCoordinates.of(PROFILE_INDEX));
        return savedProfileDocument;

    }

    @Override
    public ProfileDocument findById(String id) {
        log.info(String.format("Fetching ProfileDocument with id %s ", id));
        /*return profileDocumentRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Profile with id %s not found", id)));*/
        Query query = new StringQuery("{\"match\":{\"id\":{\"query\":\"" + id + "\"}}}\"");
        Optional<ProfileDocument> profileDocumentOptional = elasticsearchOperations.search(query, ProfileDocument.class, IndexCoordinates.of(PROFILE_INDEX)).stream().findFirst().map(SearchHit::getContent);
        return profileDocumentOptional.orElseThrow(() -> new RuntimeException(String.format("No ProfileDocument found with id %s", id)));

    }

    @Override
    public ProfileDocument updateProfile(ProfileDocument profileDocument) {
        ProfileDocument existingProfileDocument = findById(profileDocument.getId());

        /*if (Optional.ofNullable(existingProfileDocument).isPresent() {
            profileDocumentRepository.save(profileDocument);
        }*/

        if (Optional.ofNullable(existingProfileDocument).isPresent()) {
            elasticsearchOperations.save(profileDocument, IndexCoordinates.of(PROFILE_INDEX));
        }
        return findById(profileDocument.getId());
    }

    @Override
    public List<ProfileDocument> findAllProfiles() {
        /*return StreamSupport.stream(profileDocumentRepository.findAll().spliterator(), true).collect(Collectors.toList());*/
        SearchHits<ProfileDocument> searchHits = elasticsearchOperations.search(Query.findAll(), ProfileDocument.class, IndexCoordinates.of(PROFILE_INDEX));
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public List<ProfileDocument> findProfilesByTechnologyName(String technologyName) {
        //return profileDocumentRepository.findByTechnologiesName(technologyName);
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("technologies.name", technologyName);
        Query query = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();
        SearchHits<ProfileDocument> searchHits = elasticsearchOperations.search(query, ProfileDocument.class, IndexCoordinates.of(PROFILE_INDEX));
        return searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
    }

    @Override
    public void deleteProfileById(String id) {
        // profileDocumentRepository.deleteById(id);
        elasticsearchOperations.delete(id, IndexCoordinates.of(PROFILE_INDEX));
    }

}
