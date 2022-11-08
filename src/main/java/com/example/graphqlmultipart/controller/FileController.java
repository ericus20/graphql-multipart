package com.example.graphqlmultipart.controller;

import com.example.graphqlmultipart.response.FileUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Controller
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @MutationMapping
    public FileUploadResult fileUpload(@Argument MultipartFile file) {
        logger.info("Upload file: name={}", file.getOriginalFilename());

        return new FileUploadResult(UUID.randomUUID());
    }
}
