package com.paymentchain.product.business.transaction;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.paymentchain.product.entities.Product;
import com.paymentchain.product.exception.BusinessRuleException;
import com.paymentchain.product.respository.ProductRepository;

@Service
public class BusinessTransaction {

    @Autowired
    ProductRepository productRepository;

    public Product post(@RequestBody Product input) throws BusinessRuleException,UnknownHostException {
        Product dto = input;
        if(
            dto.getName() ==null ||
            dto.getName().isBlank() ||
            dto.getCode() ==null ||
            dto.getCode().isBlank() 
        ){
            BusinessRuleException businessRuleException = new BusinessRuleException(
                "1025",
                 "Error de validacion, no se permiten campos vacios",
                 HttpStatus.PRECONDITION_REQUIRED
                 );
            throw businessRuleException;
        }
        Product save = productRepository.save(dto);
        return save;
    }
}
