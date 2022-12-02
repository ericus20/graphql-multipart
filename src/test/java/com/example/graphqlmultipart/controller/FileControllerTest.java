package com.example.graphqlmultipart.controller;

import com.example.graphqlmultipart.response.FileUploadResult;
import graphql.ErrorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest
@AutoConfigureHttpGraphQlTester
class FileControllerTest {

    @Autowired
    private HttpGraphQlTester tester;

    @Test
    void missingFile() {
        tester.documentName("fileUpload")
                .variable("operations", "{ \"query\": \"mutation FileUpload($file: Upload!) {fileUpload(file: $file){id}}\" , \"variables\": {\"file\": null}}")
                .variable("map", "{\"file\": [\"variables.file\"]}")
                .execute()
                .errors()
                .satisfy(responseErrors -> {
                    Assertions.assertEquals(1, responseErrors.size());
                    Assertions.assertEquals(ErrorType.ValidationError, responseErrors.get(0).getErrorType());

                    String expected = "Variable 'file' has an invalid value: Variable 'file' has coerced Null value for NonNull type 'Upload!'";
                    Assertions.assertEquals(expected, responseErrors.get(0).getMessage());
                });

    }

    @Test
    void invalidPath() {
        tester.documentName("fileUpload")
                .variable("operations", "{ \"query\": \"mutation FileUpload($file: Upload!) {fileUpload(file: $file){id}}\" , \"variables\": {\"file\": null}}")
                .variable("file", "/Users/eopoku/Downloads/istockphoto-517188688-612x612.jpg")
                .variable("map", "{\"file\": [\"variables.file\"]}")
                .execute()
                .path("invalidPath")
                .pathDoesNotExist();

    }

    @Test
    void test() {
        tester.documentName("fileUpload")
                .variable("operations", "{ \"query\": \"mutation FileUpload($file: Upload!) {fileUpload(file: $file){id}}\" , \"variables\": {\"file\": null}}")
                .variable("file", "/Users/eopoku/Downloads/istockphoto-517188688-612x612.jpg")
                .variable("map", "{\"file\": [\"variables.file\"]}")
                .execute()
                .path("fileUpload")
                .entity(FileUploadResult.class)
                .satisfies(fileUploadResult -> Assertions.assertNotNull(fileUploadResult.getId()));

    }

    @Test
    void testAndVerify() {
        tester.documentName("fileUpload")
                .variable("operations", "{ \"query\": \"mutation FileUpload($file: Upload!) {fileUpload(file: $file){id}}\" , \"variables\": {\"file\": null}}")
                .variable("file", "/Users/eopoku/Downloads/istockphoto-517188688-612x612.jpg")
                .variable("map", "{\"file\": [\"variables.file\"]}")
                .executeAndVerify();

    }
}