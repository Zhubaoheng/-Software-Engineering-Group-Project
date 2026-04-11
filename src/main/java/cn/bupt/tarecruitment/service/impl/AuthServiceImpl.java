package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.repository.UserRepository;
import cn.bupt.tarecruitment.service.AuthService;

import java.util.Optional;
import java.util.UUID;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<AuthUser> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword() != null && user.getPassword().equals(password));
    }

    @Override
    public AuthUser registerTaUser(String username, String password) {
        String normalizedUsername = username == null ? "" : username.trim();
        String normalizedPassword = password == null ? "" : password.trim();
        if (normalizedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (normalizedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (userRepository.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("That username is already in use.");
        }

        AuthUser user = new AuthUser();
        user.setId("u-ta-" + UUID.randomUUID().toString().substring(0, 8));
        user.setUsername(normalizedUsername);
        user.setPassword(normalizedPassword);
        user.setRole("TA");
        userRepository.save(user);
        return user;
    }
}
