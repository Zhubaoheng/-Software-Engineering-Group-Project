package cn.bupt.tarecruitment.service;

import cn.bupt.tarecruitment.model.AuthUser;

import java.util.Optional;

public interface AuthService {
    Optional<AuthUser> login(String username, String password);

    AuthUser registerTaUser(String username, String password);
}
