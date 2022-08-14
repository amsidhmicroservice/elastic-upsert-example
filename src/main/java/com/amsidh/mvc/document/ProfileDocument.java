package com.amsidh.mvc.document;

import com.amsidh.mvc.model.Technologies;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Document(indexName = "profile", createIndex = true)
public class ProfileDocument {

    @Id
    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Technologies> technologies;

    //@Field(type = FieldType.Object)
    private Location location;

    @Field(type = FieldType.Keyword)
    private List<String> emails;

}
