package com.winemarketv2.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "cart")
public class Cart {
    @Id
    private String id;

    @Field(value = "UserId")
    private String userId;

    @Field(value = "DataOrder")
    private Date dateOrder = new Date();

    @Field(value = "Total")
    private double total;

    @Field(value = "Status")
    private String status;
    
}
