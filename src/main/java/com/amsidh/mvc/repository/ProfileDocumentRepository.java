package com.amsidh.mvc.repository;

import com.amsidh.mvc.document.ProfileDocument;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileDocumentRepository extends ElasticsearchRepository<ProfileDocument, String> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"technologies.name\": \"?0\"}}]}}")
    List<ProfileDocument> findByTechnologiesName(String technologyName);
}
