/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.customer.controller;

import java.lang.reflect.Field;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymentchain.customer.business.transactions.BusinessTransaction;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.respository.CustomerRepository;

/**
 *
 * @author EsGaCristo
 */
@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BusinessTransaction bt;

    // @Value("${custom.activeprofile}")
    // private String profile;

    @Autowired
    private Environment env;

    @GetMapping("/check")
    public String check() {
        return "Hello your proerty value is: " + env.getProperty("custom.activeprofileName");
    }

    @GetMapping()
    public ResponseEntity<?> list() {
        List<Customer> findAll = customerRepository.findAll();
        if (findAll.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(findAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable(name = "id") long id) {
        // Luego revisas esto
        Optional<Customer> findById = customerRepository.findById(id);
        if (findById.isPresent()) {
            return ResponseEntity.ok(findById);
        } else {
            // return ResponseEntity.notFound();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @GetMapping("/iban")
    public ResponseEntity<?> get(@RequestParam(name = "iban") String iban) {
        Optional<Customer> findByIban = Optional.ofNullable(customerRepository.findByAccount(iban));
        if (findByIban.isPresent()) {
            return ResponseEntity.ok(findByIban);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable(name = "id") long id, @RequestBody Customer input) {
        Customer find = customerRepository.findById(id).orElse(null);

        if (find == null){ return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
        
        find.setCode(input.getCode());
        find.setName(input.getName());
        find.setIban(input.getIban());
        find.setPhone(input.getPhone());
        find.setBalance(input.getBalance());
        find.setSurname(input.getSurname());

        Customer save = customerRepository.save(find);
        return ResponseEntity.ok(save);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer input) throws BusinessRuleException, UnknownHostException {
        Customer post = bt.post(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        Optional<Customer> findById = customerRepository.findById(id);
        if (findById.get() != null) {
            customerRepository.delete(findById.get());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/full")
    public ResponseEntity<?> getByCode(@RequestParam(name = "code") String code) {
        Optional<Customer> customer = Optional.ofNullable(bt.get(code));
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(customer);
    }
}
