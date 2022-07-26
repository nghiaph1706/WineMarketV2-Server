package com.winemarketv2.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "brand")
public class Brand {
    @Id
    private String id;

    @Field(value = "Name")
    private String name;

    @Field(value = "CategoryId")
    private String categoryId;
}
