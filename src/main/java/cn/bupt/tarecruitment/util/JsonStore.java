package cn.bupt.tarecruitment.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JsonStore<T> {
    private final Path file;
    private final Class<T> itemType;
    private final Function<T, String> idExtractor;
    private final ObjectMapper mapper;

    public JsonStore(Path file, Class<T> itemType, Function<T, String> idExtractor) {
        this.file = file;
        this.itemType = itemType;
        this.idExtractor = idExtractor;
        this.mapper = new ObjectMapper().findAndRegisterModules();
    }

    public synchronized List<T> readAll() {
        ensureFile();
        try {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, itemType);
            List<T> items = mapper.readValue(file.toFile(), type);
            return items == null ? new ArrayList<>() : new ArrayList<>(items);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + file, e);
        }
    }

    public synchronized void writeAll(List<T> items) {
        ensureParent();
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), items == null ? Collections.emptyList() : items);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write " + file, e);
        }
    }

    public synchronized Optional<T> findById(String id) {
        return readAll().stream().filter(item -> id.equals(idExtractor.apply(item))).findFirst();
    }

    public synchronized T save(T item) {
        List<T> items = readAll();
        String id = idExtractor.apply(item);
        boolean replaced = false;
        for (int i = 0; i < items.size(); i++) {
            if (id.equals(idExtractor.apply(items.get(i)))) {
                items.set(i, item);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            items.add(item);
        }
        writeAll(items);
        return item;
    }

    public synchronized void deleteById(String id) {
        List<T> items = readAll();
        items.removeIf(item -> id.equals(idExtractor.apply(item)));
        writeAll(items);
    }

    private void ensureFile() {
        ensureParent();
        if (Files.notExists(file)) {
            writeAll(Collections.emptyList());
        }
    }

    private void ensureParent() {
        Path parent = file.getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to create directory " + parent, e);
            }
        }
    }
}

