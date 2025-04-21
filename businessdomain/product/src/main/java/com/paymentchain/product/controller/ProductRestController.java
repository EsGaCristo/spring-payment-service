/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.product.controller;

import com.paymentchain.product.business.transaction.BusinessTransaction;
import com.paymentchain.product.entities.Product;
import com.paymentchain.product.exception.BusinessRuleException;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.UnknownHostException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.product.respository.ProductRepository;
import java.util.Optional;

/**
 *
 * @author EsGaCristo
 */
@RestController
@RequestMapping("/product")
public class ProductRestController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BusinessTransaction bt;

    @GetMapping()
    public ResponseEntity<?> list() {
        List<Product> findAll = productRepository.findAll(); 
        if (findAll.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(findAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Product>> get(@PathVariable(name = "id") long id) {
        Optional<Product> findById = productRepository.findById(id);
        if (findById.isPresent()) {
            return ResponseEntity.ok(findById);    
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Product input) {
        Product find = productRepository.findById(id).get();
        if (find != null) {
            find.setCode(input.getCode());
            find.setName(input.getName());
        }
        Product save = productRepository.save(find);
        return ResponseEntity.ok(save);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Product input) throws BusinessRuleException,UnknownHostException {
        Product post = bt.post(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        Optional<Product> findById = productRepository.findById(id);
        if (findById.get() != null) {
            productRepository.delete(findById.get());
        }
        return ResponseEntity.ok().build();
    }

}
