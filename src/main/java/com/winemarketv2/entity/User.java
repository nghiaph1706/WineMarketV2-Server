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
@Document(value = "user")
public class User {
    @Id
    private String id;

    @Field(value = "Username")
    private String username;

    @Field(value = "Password")
    private String password;

    @Field(value = "Email")
    private String email;

    @Field(value = "Image")
    private String image;

    @Field(value = "Role")
    private boolean role;
}
