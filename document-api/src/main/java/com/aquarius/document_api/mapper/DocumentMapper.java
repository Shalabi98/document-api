package com.aquarius.document_api.mapper;

import com.aquarius.document_api.domain.Document;
import com.aquarius.document_api.dto.DocumentRequest;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    /**
     * Converts a validated request DTO into a Database Entity.
     */
    public Document toEntity(DocumentRequest request) {
        if (request == null) {
            return null;
        }

        Document document = new Document();
        document.setDocumentType(request.getDocumentType());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setDocumentDate(request.getDocumentDate());
        document.setIssuerRuc(request.getIssuerRuc());
        document.setAmount(request.getAmount());
        document.setImageBase64(request.getImageBase64());

        // Note: 'id' and 'createdAt' are handled by JPA automatically
        return document;
    }
}