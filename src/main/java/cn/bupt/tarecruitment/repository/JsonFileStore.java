package cn.bupt.tarecruitment.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonFileStore {
    private final ObjectMapper mapper;

    public JsonFileStore() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public <T> List<T> readList(String relativePath, TypeReference<List<T>> typeReference) {
        Path path = resolvePath(relativePath);
        if (Files.exists(path)) {
            try {
                return mapper.readValue(path.toFile(), typeReference);
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to read " + relativePath, e);
            }
        }

        try (InputStream in = getClassLoaderResource(relativePath)) {
            if (in != null) {
                return mapper.readValue(in, typeReference);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read classpath resource " + relativePath, e);
        }

        return new ArrayList<>();
    }

    public void writeList(String relativePath, Object value) {
        Path path = resolvePath(relativePath);
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            mapper.writeValue(path.toFile(), value);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write " + relativePath, e);
        }
    }

    private Path resolvePath(String relativePath) {
        return Paths.get(relativePath);
    }

    private InputStream getClassLoaderResource(String relativePath) {
        String normalized = relativePath.startsWith("/") ? relativePath.substring(1) : relativePath;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(normalized);
    }
}
