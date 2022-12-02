package com.example.graphqlmultipart.controller;

import com.example.graphqlmultipart.config.FileDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class FileControllerTest {

    @Autowired
    private HttpGraphQlTester tester;

    @Test
    void test() throws Exception {

        var file = new FileDTO("/Users/eopoku/Downloads/istockphoto-517188688-612x612.jpg");

        tester.documentName("fileUpload")
                .variable("operations", "{ \"query\": \"mutation FileUpload($file: Upload!) {fileUpload(file: $file){id}}\" , \"variables\": {\"file\": null}}")
                .variable("file", "/Users/eopoku/Downloads/istockphoto-517188688-612x612.jpg")
                .variable("map", "{\"file\": [\"variables.file\"]}")
                .execute()
                .errors()
                .satisfy(System.out::println);

    }
}