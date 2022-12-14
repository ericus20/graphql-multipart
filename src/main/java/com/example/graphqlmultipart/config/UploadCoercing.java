package com.example.graphqlmultipart.config;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * To transform the binary part to a MultipartFile.
 */
public class UploadCoercing implements Coercing<MultipartFile, MultipartFile> {

    @Override
    public MultipartFile serialize(Object dataFetcherResult) throws CoercingSerializeException {
        throw new CoercingSerializeException("Upload is an input-only type");
    }

    @Override
    public MultipartFile parseValue(Object input) throws CoercingParseValueException {
        if (input instanceof MultipartFile) {
            return (MultipartFile) input;
        }
        // To support integration testing
        if (input instanceof String && isValidFile(input)) {
            var fileDto = new FileDTO((String) input);

            return fileDto.toMultipartFile();
        }
        throw new CoercingParseValueException(
                String.format("Expected a 'MultipartFile' like object but was '%s'.", input != null ? input.getClass() : null)
        );
    }

    @Override
    public MultipartFile parseLiteral(Object input) throws CoercingParseLiteralException {
        throw new CoercingParseLiteralException("Parsing literal of 'MultipartFile' is not supported");
    }

    private boolean isValidFile(Object input) {
        var filePath = (String) input;

        return StringUtils.hasText(filePath)
                && Files.exists(Paths.get(filePath));
    }
}
