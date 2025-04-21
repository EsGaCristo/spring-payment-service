/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.product.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author cristo
 */
@Schema(description = "This model is used to return errors in RFC 7807 which created a generalized error-handling schema composed by five parts")
@NoArgsConstructor
@Data
public class StandarizedApiExceptionResponse {
    
    @Schema(description = "This unique uir identifier that categorizes the errro",name ="type",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "/errors/autentication/not-authorized"
            )
    private String type;
    
    @Schema(description = "A brief, human-readable message about the error",name ="tittle",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "the user does not have authorization"
            )
    private String title;
    
    @Schema(description = "The unique error code",name ="code",
            required = false, example = "192"
            )
    private String code;
    
    @Schema(description = "A human-readable explanation of the error",name ="detail",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "The user does not have the propertly permissions to acces the "
            + "resource, please contact with us https://jijijjja.com"
            )
    private String detail;
    
    @Schema(description = "A URI that identifies the specific ocurrence of the error",name ="detail",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "/errors/authentication/not-authorized/01"
            )
    private String instance;

    public StandarizedApiExceptionResponse(String type, String title, String code, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.code = code;
        this.detail = detail;
        this.instance = instance;
    }

    
    
}
