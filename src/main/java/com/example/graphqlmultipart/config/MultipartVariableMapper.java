package com.example.graphqlmultipart.config;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Maps http request's file to GraphQL variables.
 */
public class MultipartVariableMapper {

    private static final Pattern PERIOD = Pattern.compile("\\.");

    private static final Mapper<Map<String, Object>> MAP_MAPPER =
            new Mapper<>() {
                @Override
                public Object set(Map<String, Object> location, String target, MultipartFile value) {
                    return location.put(target, value);
                }

                @Override
                public Object recurse(Map<String, Object> location, String target) {
                    return location.get(target);
                }
            };
    private static final Mapper<List<Object>> LIST_MAPPER =
            new Mapper<>() {
                @Override
                public Object set(List<Object> location, String target, MultipartFile value) {
                    return location.set(Integer.parseInt(target), value);
                }

                @Override
                public Object recurse(List<Object> location, String target) {
                    return location.get(Integer.parseInt(target));
                }
            };

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void mapVariable(final String objectPath, final Map<String, Object> variables, final MultipartFile part) {
        var segments = PERIOD.split(objectPath);

        if (segments.length < 2) {
            throw new RuntimeException("object-path in map must have at least two segments");
        } else if (!"variables".equals(segments[0])) {
            throw new RuntimeException("can only map into variables");
        }

        Object currentLocation = variables;
        for (int i = 1; i < segments.length; i++) {
            var segmentName = segments[i];
            Mapper mapper = determineMapper(currentLocation, objectPath, segmentName);

            if (i == segments.length - 1) {
                if (Objects.nonNull(mapper.set(currentLocation, segmentName, part))) {
                    throw new RuntimeException("expected null value when mapping " + objectPath);
                }
            } else {
                currentLocation = mapper.recurse(currentLocation, segmentName);
                if (Objects.isNull(currentLocation)) {
                    throw new RuntimeException(
                            "found null intermediate value when trying to map " + objectPath);
                }
            }
        }
    }

    private static Mapper<?> determineMapper(final Object currentLocation, final String objectPath, final String segmentName) {
        if (currentLocation instanceof Map) {
            return MAP_MAPPER;
        } else if (currentLocation instanceof List) {
            return LIST_MAPPER;
        }

        throw new RuntimeException(
                "expected a map or list at " + segmentName + " when trying to map " + objectPath);
    }

    interface Mapper<T> {

        Object set(final T location, final String target, final MultipartFile value);

        Object recurse(final T location, final String target);
    }
}
