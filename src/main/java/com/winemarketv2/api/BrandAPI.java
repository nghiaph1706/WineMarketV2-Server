package com.winemarketv2.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winemarketv2.Repository.BrandRepository;
import com.winemarketv2.entity.Brand;

@RestController
@RequestMapping("/api/v1/brand/")
public class BrandAPI {
    @Autowired
    BrandRepository brandRepository;

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrand(){
        List<Brand> bList = brandRepository.findAll();
        if (bList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Brand>>(bList,HttpStatus.OK);
    }

    @GetMapping(value = "/CategoryId/{id}")
    public ResponseEntity<List<Brand>> getByCategoryId(@PathVariable("id") String id){
        List<Brand> bList = brandRepository.findByCategoryId(id);
        if (bList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<Brand>>(bList,HttpStatus.OK);
    }
}
