package cn.bupt.tarecruitment.repository;

import cn.bupt.tarecruitment.util.JsonUtils;

import java.nio.file.Path;
import java.util.List;

public abstract class JsonRepository<T> {
    private final Path file;
    private final Class<T> type;

    protected JsonRepository(Path file, Class<T> type) {
        this.file = file;
        this.type = type;
    }

    protected synchronized List<T> readAll() {
        return JsonUtils.readList(file, type);
    }

    protected synchronized void writeAll(List<T> items) {
        JsonUtils.writeList(file, items);
    }
}
