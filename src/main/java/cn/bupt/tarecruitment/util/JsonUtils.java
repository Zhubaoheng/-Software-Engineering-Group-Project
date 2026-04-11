package cn.bupt.tarecruitment.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private JsonUtils() {
    }

    public static synchronized <T> List<T> readList(Path file, Class<T> elementType) {
        ensureFile(file);
        try {
            byte[] bytes = Files.readAllBytes(file);
            if (bytes.length == 0) {
                return new ArrayList<>();
            }
            JavaType type = MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
            List<T> result = MAPPER.readValue(bytes, type);
            return result == null ? new ArrayList<>() : new ArrayList<>(result);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read JSON from " + file, e);
        }
    }

    public static synchronized <T> void writeList(Path file, List<T> values) {
        ensureParent(file);
        try {
            MAPPER.writeValue(file.toFile(), values == null ? List.of() : values);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write JSON to " + file, e);
        }
    }

    public static synchronized void ensureFile(Path file) {
        ensureParent(file);
        if (Files.notExists(file)) {
            // Try to copy seed data from classpath (src/main/resources/data/)
            String fileName = file.getFileName().toString();
            try (var in = JsonUtils.class.getResourceAsStream("/data/" + fileName)) {
                if (in != null) {
                    Files.copy(in, file);
                } else {
                    Files.writeString(file, "[]", StandardCharsets.UTF_8);
                }
            } catch (IOException e) {
                try {
                    Files.writeString(file, "[]", StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    throw new IllegalStateException("Failed to create JSON file " + file, ex);
                }
            }
        }
    }

    public static synchronized void ensureDir(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create directory " + dir, e);
        }
    }

    private static void ensureParent(Path file) {
        Path parent = file.getParent();
        if (parent != null) {
            ensureDir(parent);
        }
    }
}
