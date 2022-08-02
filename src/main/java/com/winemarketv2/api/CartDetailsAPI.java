package com.winemarketv2.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winemarketv2.Repository.CartDetailsRepository;
import com.winemarketv2.Repository.CartRepository;
import com.winemarketv2.Repository.ProductRepository;
import com.winemarketv2.entity.Cart;
import com.winemarketv2.entity.CartDetails;
import com.winemarketv2.entity.Product;

@RestController
@RequestMapping("/api/v1/cartdetails/")
// @PreAuthorize("(isAuthenticated() and #username == authentication.principal.username) or hasAuthority('ADMIN')")
@PreAuthorize("isAuthenticated()")
public class CartDetailsAPI {
    @Autowired
    CartDetailsRepository cartDetailsRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;
    
    @GetMapping(value = "CartId/{id}")
    public ResponseEntity<List<Object>> getById(@PathVariable("id") String id) {
        List<Object> list = new ArrayList<>();
        List<CartDetails> cList = cartDetailsRepository.findByCartId(id);
        if (cList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (CartDetails cartDetails : cList) {
            HashMap<String, String> data= new HashMap<>();
            Product product = productRepository.findById(cartDetails.getProductId()).get();
            data.put("productImage", product.getImage());
            data.put("productName", product.getName());
            data.put("productPrice", String.valueOf(product.getPrice()));
            data.put("quantity", String.valueOf(cartDetails.getQuantity()));
            data.put("total", String.valueOf(cartDetails.getTotal()));
            list.add(data);
        } 
        return new ResponseEntity<List<Object>>(list, HttpStatus.OK);
    }

    @PostMapping
    public HttpStatus addToCart(@Valid @RequestBody CartDetails cartDetails){
        CartDetails cDetailsn = cartDetailsRepository.findByCartIdAndProductId(cartDetails.getCartId(), cartDetails.getProductId());
        if (cDetailsn == null) {
            cDetailsn = new CartDetails();
            cDetailsn.setCartId(cartDetails.getCartId());
            cDetailsn.setProductId(cartDetails.getProductId());
            cDetailsn.setQuantity(cartDetails.getQuantity());
            cDetailsn.setTotal(cartDetails.getTotal());
            cartDetailsRepository.save(cDetailsn);
        } else {
            cDetailsn.setQuantity(cDetailsn.getQuantity() + cartDetails.getQuantity());
            cDetailsn.setTotal(cDetailsn.getTotal() + cartDetails.getTotal());
            cartDetailsRepository.save(cDetailsn);
        }
        Cart cart = cartRepository.findById(cDetailsn.getCartId()).get();
        List<CartDetails> cList = cartDetailsRepository.findAllByCartId(cDetailsn.getCartId());
        double total =0 ;
        for (CartDetails c : cList) {
            total += c.getTotal();
        }
        cart.setTotal(total);
        cartRepository.save(cart);
        return HttpStatus.OK;
    }

}
