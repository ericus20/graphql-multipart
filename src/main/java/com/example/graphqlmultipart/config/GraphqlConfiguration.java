package com.example.graphqlmultipart.config;

import graphql.schema.GraphQLScalarType;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static com.example.graphqlmultipart.config.GraphqlMultipartHandler.SUPPORTED_RESPONSE_MEDIA_TYPES;
import static org.springframework.http.MediaType.*;

@Configuration
public class GraphqlConfiguration {

    /**
     * Register a new scalar type 'Upload'.
     * This can then be defined in the schema as 'scalar Upload'
     *
     * @return RuntimeWiringConfigurer
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurerUpload() {

        GraphQLScalarType uploadScalar = GraphQLScalarType.newScalar()
                .name("Upload")
                .coercing(new UploadCoercing())
                .build();

        return wiringBuilder -> wiringBuilder.scalar(uploadScalar);
    }

    /**
     * RouterFunctions are Central entry point to Spring's functional web framework.
     * Order(1) means this Should be before the [com.expediagroup.graphql.spring.RoutesConfiguration#graphQLRoutes].
     * <p>
     * Configuring the graphql route specified as a POST request and support for multipart from GraphqlMultipartHandler.
     *
     * @param properties              GraphQlProperties
     * @param graphqlMultipartHandler GraphqlMultipartHandler
     * @return RouterFunction<ServerResponse>
     */
    @Bean
    @Order(1)
    public RouterFunction<ServerResponse> graphQlMultipartRouterFunction(
            GraphQlProperties properties,
            GraphqlMultipartHandler graphqlMultipartHandler
    ) {
        String path = properties.getPath();
        RouterFunctions.Builder builder = RouterFunctions.route();
        builder = builder.POST(path, RequestPredicates.contentType(MULTIPART_FORM_DATA, APPLICATION_GRAPHQL, APPLICATION_JSON)
                .and(RequestPredicates.accept(SUPPORTED_RESPONSE_MEDIA_TYPES.toArray(MediaType[]::new))), graphqlMultipartHandler::handleRequest);
        return builder.build();
    }
}
