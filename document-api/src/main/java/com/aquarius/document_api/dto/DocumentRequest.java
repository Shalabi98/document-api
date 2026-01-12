package com.aquarius.document_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Data
public class DocumentRequest {

    @NotBlank(message = "Document type is required (e.g., INVOICE, RECEIPT)")
    private String documentType;

    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotNull(message = "Document date is required")
    private LocalDate documentDate;

    @NotBlank(message = "Issuer RUC is required")
    @Size(min = 11, max = 11, message = "RUC must be exactly 11 digits")
    @Pattern(regexp = "\\d+", message = "RUC must contain only digits")
    private String issuerRuc;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Base64 image string is required")
    private String imageBase64;
}