/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.transactions.exception;

import com.paymentchain.transactions.common.StandarizedApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author cristo
 */
@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknowException(Exception ex){
        StandarizedApiExceptionResponse standarizedApiExceptionResponse = new StandarizedApiExceptionResponse(
                "TECNICO",
                "ERROR DE ENTRADA Y SALIDA DE DATOS", 
                "1024",
                ex.getMessage(),
                null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standarizedApiExceptionResponse);
    }
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<?> handleBusinessRuleException(BusinessRuleException ex){
        StandarizedApiExceptionResponse standarizedApiExceptionResponse = new StandarizedApiExceptionResponse(
                "BUSSINES ",
                "Error de validacion", 
                ex.getCode(),
                ex.getMessage(),
                "");
        return ResponseEntity.status(ex.getHttpStatus()).body(standarizedApiExceptionResponse);
    }
}
