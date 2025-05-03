/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.billing.controller;

import com.paymentchain.billing.common.InvoiceRequestMapper;
import com.paymentchain.billing.common.InvoiceResponseMapper;
import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.dto.InvoiceResponse;
import com.paymentchain.billing.entities.Invoice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import com.paymentchain.billing.respository.InvoiceRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 *
 * @author sotobotero
 */
@Tag(name = "Billing API",description = "This API serve all functionality for managment invoices")
@RestController
@RequestMapping("/billing")
public class InvoiceRestController {
    
    @Autowired
    InvoiceRepository billingRepository;

    @Autowired
    InvoiceRequestMapper requestMapper;

    @Autowired
    InvoiceResponseMapper responseMapper;

    
    @Operation(description = "Return all Invoices bundled into response", summary = "Return 204 if not data found")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Exito"),
        @ApiResponse(responseCode = "500",description = "Internal Error"),
    })
    @GetMapping()
    public List<InvoiceResponse> list() {
        List<Invoice> findAll =  billingRepository.findAll();
        return responseMapper.InvoiceListToInvoiceResponseList(findAll);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?>  get(@PathVariable long id) {
          Optional<Invoice> invoice = billingRepository.findById(id);
        if (invoice.isPresent()) {
            InvoiceResponse invoiceToInvoiceResponse = responseMapper.InvoiceToInvoiceResponse(invoice.get());
            return new ResponseEntity<>(invoiceToInvoiceResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable String id, @RequestBody InvoiceRequest input) {
        Invoice save = null;
        Optional<Invoice>  findById = billingRepository.findById(Long.valueOf(id));
        Invoice get = findById.get();
        if(get !=null){
            save = billingRepository.save(get);
        } 

        return ResponseEntity.ok(save);
    }
    
    @PostMapping
    public ResponseEntity<?> post(@RequestBody InvoiceRequest input) {
        Invoice InvoiceRequestToInvoice = requestMapper.InvoiceRequestToInvoice(input);
        Invoice save = billingRepository.save(InvoiceRequestToInvoice);
        InvoiceResponse InvoiceToInvoiceResponse = responseMapper.InvoiceToInvoiceResponse(save);
        return ResponseEntity.ok(InvoiceToInvoiceResponse);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
         Optional<Invoice> dto = billingRepository.findById(Long.valueOf(id));
        if (!dto.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        billingRepository.delete(dto.get());
        return ResponseEntity.ok().build();
    }
    
}
