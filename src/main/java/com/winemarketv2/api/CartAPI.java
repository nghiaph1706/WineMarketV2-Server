package com.winemarketv2.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winemarketv2.Repository.CartRepository;
import com.winemarketv2.entity.Cart;

@RestController
@RequestMapping("/api/v1/cart/")
public class CartAPI {
    @Autowired
    CartRepository cartRepository;

    @GetMapping(value = "/{id}")
    @PreAuthorize("(isAuthenticated() and #username == authentication.principal.username) or hasAuthority('ADMIN')")
    public ResponseEntity<Cart> getCart(@PathVariable("id") String id){
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PostMapping(value = "checkout/{id}")
    @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public ResponseEntity<Object> checkout(@PathVariable("id") String id, @RequestParam("user_Id") String userId){
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        cart.setStatus("Pending");
        cartRepository.save(cart);
        cart = new Cart();
        cart.setUserId(userId);
        cart.setTotal(0);
        cart.setStatus("None");
        cartRepository.save(cart);
        String cartId = cartRepository.findByUserIdAndStatusLike(userId, "None").getId();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("cartId", cartId)); 
    }
}
