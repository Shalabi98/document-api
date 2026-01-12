package com.aquarius.document_api.service;

import com.aquarius.document_api.domain.Document;
import com.aquarius.document_api.repository.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class DocumentService {

    private final DocumentRepository repository;

    // Thread-safe list to hold active frontend connections
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    // 1. Persist the document and broadcasts the event to connected clients
    public Document ingest(Document document) {
        log.info("Saving new document: {} - {}", document.getDocumentType(), document.getDocumentNumber());

        // Store the document
        Document savedDoc = repository.saveAndFlush(document);

        // Trigger Real-time notification
        notifySubscribers(savedDoc);

        return savedDoc;
    }

    // 2. Create a new SSE (Server-Sent Event) connection for a client
    public SseEmitter subscribe() {
        // Set timeout and append client connection
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.add(emitter);

        // Cleanup hooks
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            this.emitters.remove(emitter);
        });

        return emitter;
    }

    // 3. Notification Logic
    private void notifySubscribers(Document document) {
        if (emitters.isEmpty())
            return;

        log.debug("Broadcasting new document to {} active clients", emitters.size());

        // Iterate and remove "dead" clients who disconnected unexpectedly
        emitters.removeIf(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("new-document")
                        .data(document));
                return false; // Connection is alive, keep it
            } catch (IOException e) {
                return true; // Connection is dead, remove it
            }
        });
    }
}
