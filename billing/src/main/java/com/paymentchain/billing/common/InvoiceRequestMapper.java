package com.paymentchain.billing.common;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.paymentchain.billing.dto.InvoiceRequest;
import com.paymentchain.billing.entities.Invoice;

@Mapper(componentModel = "spring")
public interface InvoiceRequestMapper {
    // Mapeo de objeto a request
    @Mappings({
        @Mapping(source = "customer", target = "customerId")
    })
    Invoice InvoiceRequestToInvoice(InvoiceRequest source);
    //No es necesario poner la notacion Mappin, con una es suficiente
    List<Invoice> InvoiceRequestToInvoiceList(List<InvoiceRequest> source);
    
    @InheritInverseConfiguration
    InvoiceRequest InvoiceToInvoiceRequest(Invoice source);

    @InheritInverseConfiguration
    List<InvoiceRequest> InvoiceToInvoiceResponse(List<Invoice> source);
}
