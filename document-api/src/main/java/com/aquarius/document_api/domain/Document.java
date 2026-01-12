package com.aquarius.document_api.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Data
@Table(name = "DOCUMENT")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Document type is required")
    @Column(name = "document_type", nullable = false)
    private String documentType;

    @NotNull(message = "Document number is required")
    @Column(name = "document_number")
    private String documentNumber;

    @NotNull(message = "Date is required")
    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @NotBlank
    @Size(min = 11, max = 11, message = "RUC must be 11 digits")
    @Column(name = "issuer_ruc", length = 11, nullable = false)
    private String issuerRuc;

    // Requirement: Positive amount
    @NotNull
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank
    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}