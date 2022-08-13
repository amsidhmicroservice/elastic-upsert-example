package com.amsidh.mvc.service.impl;

import com.amsidh.mvc.document.ProfileDocument;
import com.amsidh.mvc.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.amsidh.mvc.util.Constant.INDEX;
import static com.amsidh.mvc.util.Constant.TYPE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    @Override
    public String createProfileDocument(ProfileDocument profileDocument) throws IOException {
        UUID uuid = UUID.randomUUID();
        profileDocument.setId(uuid.toString());
        Map documentMapper = objectMapper.convertValue(profileDocument, Map.class);

        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, profileDocument.getId());
        indexRequest.source(documentMapper);

        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getResult().name();
    }

    @Override
    public ProfileDocument findById(String id) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        return objectMapper.convertValue(getResponse.getSource(), ProfileDocument.class);
    }

    @Override
    public String updateProfile(ProfileDocument profileDocument) throws IOException {
        ProfileDocument oldProfileDocument = findById(profileDocument.getId());
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, oldProfileDocument.getId());
        Map profileDocumentMap = objectMapper.convertValue(profileDocument, Map.class);
        updateRequest.doc(profileDocumentMap);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        return updateResponse.getResult().name();
    }

    @Override
    public List<ProfileDocument> findAllProfiles() throws IOException {
        SearchRequest searchRequest = buildSearchRequest(INDEX, TYPE);
        searchRequest.source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchAllQuery()));

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);
    }

    @Override
    public List<ProfileDocument> findProfilesByTechnologyName(String technologyName) throws IOException {
        SearchRequest searchRequest = buildSearchRequest(INDEX, TYPE);
        searchRequest.source(SearchSourceBuilder.searchSource()
                .query(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("technologies.name", technologyName))));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);
    }

    @Override
    public String deleteProfileById(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE);
        deleteRequest.id(id);
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return deleteResponse.getResult().name();
    }


    private List<ProfileDocument> getSearchResult(SearchResponse searchResponse) {
        return Arrays.stream(searchResponse.getHits().getHits())
                .map(searchHit -> {
                    return objectMapper.convertValue(searchHit.getSourceAsMap(), ProfileDocument.class);
                })
                .collect(Collectors.toList());
    }

    private SearchRequest buildSearchRequest(String indexName, String documentType) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indexName);
        searchRequest.types(documentType);
        return searchRequest;
    }
}
