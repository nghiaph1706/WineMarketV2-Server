package com.winemarketv2.api;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.winemarketv2.Repository.CartRepository;
import com.winemarketv2.Repository.UserRepository;
import com.winemarketv2.entity.Cart;
import com.winemarketv2.service.EmailService;

@RestController
@RequestMapping("/api/v1/cart/")
public class CartAPI {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @GetMapping(value = "{id}")
    @PreAuthorize("isAuthenticated()")
    // @PreAuthorize("(isAuthenticated() and #username == authentication.principal.username) or hasAuthority('ADMIN')")
    public ResponseEntity<Cart> getCart(@PathVariable("id") String id){
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PostMapping(value = "checkout/{id}")
    @PreAuthorize("isAuthenticated()")
    // @PreAuthorize("isAuthenticated() and #username == authentication.principal.username")
    public ResponseEntity<Object> checkout(@PathVariable("id") String id, @RequestParam("user_Id") String userId) throws UnsupportedEncodingException, MessagingException{
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
        cart = cartRepository.findByUserIdAndStatusLike(userId, "None");

        Context context = new Context();
        context.setVariable("status", "pending");
        emailService.sendMail(userRepository.findById(userId).get().getEmail(), "Your order is pending", "order", context);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("cartId", cart.getId())); 
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Object>> getAllCart(){
        List<Object> data = new ArrayList<Object>();
        List<Cart> cList = cartRepository.findAll();
        if (cList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for (Cart cart : cList) {
            if (!cart.getStatus().equals("None")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cart.getId());
                map.put("username", userRepository.findById(cart.getUserId()).get().getUsername());
                map.put("date", cart.getDateOrder().toString());
                map.put("total", String.valueOf(cart.getTotal()));
                map.put("status", cart.getStatus());
                data.add(map);
            }
        }
        return new ResponseEntity<List<Object>>(data,HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public HttpStatus updateCart(@Valid @RequestBody Cart cart){
        Cart cartn = cartRepository.findById(cart.getId()).orElse(null);
        if (cartn == null) {
            return HttpStatus.NOT_FOUND;
        }
        cartn.setStatus(cart.getStatus());
        cartRepository.save(cartn);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public HttpStatus deleteCart(@PathVariable("id") String id){
        Cart cart = cartRepository.findById(id).orElse(null);
        if (cart == null) {
            return HttpStatus.NOT_FOUND;
        }
        cartRepository.delete(cart);
        return HttpStatus.OK;
    }

    @GetMapping(value = "filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Object>> filterCart(
        @RequestParam("_date") String date,
        @RequestParam("_username") String username,
        @RequestParam("_status") String status
    ){  
        Pageable pageable;
        List<Cart> cList = new ArrayList<>();
        List<Object> data = new ArrayList<Object>();
        if (date.equals("desc")) {
            pageable = PageRequest.of(0, 1000, Sort.by("dateOrder").descending());
        } else {
            pageable = PageRequest.of(0, 1000, Sort.by("dateOrder").ascending());
        }
        try {
            String userId = userRepository.findByUsername(username).getUsername();
            cList = cartRepository.findByUserIdLikeAndStatusLike(pageable, userId, status);
        } catch (Exception e) {
            cList = cartRepository.findByUserIdLikeAndStatusLike(pageable, "", status);
        }
        
        if (cList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for (Cart cart : cList) {
            if (!cart.getStatus().equals("None")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cart.getId());
                map.put("username", userRepository.findById(cart.getUserId()).get().getUsername());
                map.put("date", cart.getDateOrder().toString());
                map.put("total", String.valueOf(cart.getTotal()));
                map.put("status", cart.getStatus());
                data.add(map);
            }
        }

        return new ResponseEntity<List<Object>>(data, HttpStatus.OK);
    }
}
