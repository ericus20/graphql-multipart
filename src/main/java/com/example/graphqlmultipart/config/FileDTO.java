package com.example.graphqlmultipart.config;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileDTO {
    private final File file;
    private final byte[] contents;

    public FileDTO(String file) {
        this.file = new File(file);
        this.contents = file.getBytes(StandardCharsets.UTF_8);
    }

    public MultipartFile toMultipartFile() {
        return new BASE64DecodedMultipartFile(contents, file.getName());
    }

    public File getFile() {
        return file;
    }

    public byte[] getContents() {
        return contents;
    }
}
