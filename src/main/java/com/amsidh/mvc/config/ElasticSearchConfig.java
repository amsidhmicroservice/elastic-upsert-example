package com.amsidh.mvc.config;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticSearchConfig {


    @Bean(name = "elasticsearchTemplate")
    public ElasticsearchOperations getElasticsearchOperations(Client client) {
        ElasticsearchOperations elasticsearchOperations = new ElasticsearchTemplate(client);
        return elasticsearchOperations;
    }
}
