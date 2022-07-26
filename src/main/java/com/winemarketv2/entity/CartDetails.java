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
@Document(value = "cartDetails")
public class CartDetails {
    @Id
    private String id;

    @Field(value = "CartId")
    private String cartId;

    @Field(value = "ProductId")
    private String productId;

    @Field(value = "Quantity")
    private int quantity;

    @Field(value = "Total")
    private double total;
}
