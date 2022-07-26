package com.winemarketv2.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winemarketv2.Repository.CategoryRepository;
import com.winemarketv2.entity.Category;

@RestController
@RequestMapping("/api/v1/category/")
public class CategoryAPI {
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        List<Category> cList = categoryRepository.findAll();
        if (cList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }        
        return new ResponseEntity<List<Category>>(cList, HttpStatus.OK);
    }
}
