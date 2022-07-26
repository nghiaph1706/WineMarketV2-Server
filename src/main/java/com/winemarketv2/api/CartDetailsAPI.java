package com.winemarketv2.api;

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
import com.winemarketv2.entity.CartDetails;

@RestController
@RequestMapping("/api/v1/cartdetails/")
@PreAuthorize("(isAuthenticated() and #username == authentication.principal.username) or hasAuthority('ADMIN')")
public class CartDetailsAPI {
    @Autowired
    CartDetailsRepository cartDetailsRepository;
    
    @GetMapping(value = "CartId/{id}")
    public ResponseEntity<List<CartDetails>> getById(@PathVariable("id") String id) {
        List<CartDetails> cList = cartDetailsRepository.findByCartId(id);
        if (cList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<CartDetails>>(cList, HttpStatus.OK);
    }

    @PostMapping
    public HttpStatus addToCart(@Valid @RequestBody CartDetails cartDetails){
        CartDetails cDetailsn = cartDetailsRepository.findById(cartDetails.getId()).orElse(null);
        if (cDetailsn == null) {
            cDetailsn = new CartDetails();
            cDetailsn.setCartId(cartDetails.getCartId());
            cDetailsn.setProductId(cartDetails.getProductId());
            cDetailsn.setQuantity(cartDetails.getQuantity());
            cDetailsn.setTotal(cartDetails.getTotal());
            cartDetailsRepository.save(cDetailsn);
        } else {
            cDetailsn.setQuantity(cDetailsn.getQuantity() + cartDetails.getQuantity());
            cartDetailsRepository.save(cDetailsn);
        }
        return HttpStatus.OK;
    }

}
