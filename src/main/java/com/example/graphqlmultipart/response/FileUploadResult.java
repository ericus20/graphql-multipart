package com.example.graphqlmultipart.response;

import java.util.UUID;

public class FileUploadResult {
    UUID id;

    public FileUploadResult(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
