package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.repository.UserRepository;
import cn.bupt.tarecruitment.service.AuthService;

import java.util.Optional;

public class DefaultAuthService implements AuthService {
    private final UserRepository userRepository;

    public DefaultAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<AuthUser> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword() != null && user.getPassword().equals(password));
    }
}
