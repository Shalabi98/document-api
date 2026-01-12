package com.aquarius.document_api.controller;

import com.aquarius.document_api.domain.Document;
import com.aquarius.document_api.dto.DocumentRequest;
import com.aquarius.document_api.mapper.DocumentMapper;
import com.aquarius.document_api.service.DocumentService;
import com.aquarius.document_api.repository.DocumentRepository;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService service;
    private final DocumentRepository repository;
    private final DocumentMapper mapper;

    public DocumentController(DocumentService service, DocumentRepository repository, DocumentMapper mapper) {
        this.service = service;
        this.repository = repository;
        this.mapper = mapper;
    }

    // Endpoint 1: Receive JSON Payload from Python OCR Service
    @PostMapping
    public ResponseEntity<Document> receiveDocument(@RequestBody @Valid DocumentRequest requestDto) {
        // 1. Map Request DTO to Domain Entity
        Document entity = mapper.toEntity(requestDto);

        // 2. Delegate to Service
        Document document = service.ingest(entity);

        // 3. Return 200 OK
        return ResponseEntity.ok(document);
    }

    // Endpoint 2: Paginated Query for Angular Frontend Table
    @GetMapping
    public ResponseEntity<Page<Document>> getAllDocuments(Pageable pageable) {
        // Example: GET /api/documents?page=0&size=10&sort=createdAt,desc
        return ResponseEntity.ok(repository.findAll(pageable));
    }

    // Endpoint 3: Real-time Subscription (SSE)
    @GetMapping("/stream")
    public SseEmitter streamDocuments() {
        return service.subscribe();
    }
}