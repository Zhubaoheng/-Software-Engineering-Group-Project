package cn.bupt.tarecruitment.repository;

import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.util.AppPaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends JsonRepository<AuthUser> {
    public UserRepository() {
        super(AppPaths.USERS_FILE, AuthUser.class);
    }

    public List<AuthUser> findAll() {
        return readAll();
    }

    public Optional<AuthUser> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(user -> username.equalsIgnoreCase(user.getUsername()))
                .findFirst();
    }

    public Optional<AuthUser> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return findAll().stream()
                .filter(user -> id.equals(user.getId()))
                .findFirst();
    }

    public void save(AuthUser user) {
        List<AuthUser> users = new ArrayList<>(findAll());
        users.removeIf(item -> user.getId().equals(item.getId()));
        users.add(user);
        writeAll(users);
    }
}
