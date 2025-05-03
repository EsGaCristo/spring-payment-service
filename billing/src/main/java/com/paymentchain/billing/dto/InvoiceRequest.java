package com.paymentchain.billing.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.Data;


@Schema(name = "InvoiceRequest",description = "Model represent a invoice on database")
@Data
public class InvoiceRequest {
        @Schema(name="customer",requiredMode = RequiredMode.REQUIRED, example = "3",defaultValue = "8",description = "Unique Id of Customer")
    private long customer;
        @Schema(name="number",requiredMode = RequiredMode.REQUIRED, example = "3",defaultValue = "8",description = "Number given on fisical invoice")
    private String number;
    private String detail;
    private double amount;
}
