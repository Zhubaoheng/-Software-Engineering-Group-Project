package cn.bupt.tarecruitment.service.impl;

import cn.bupt.tarecruitment.model.AuthUser;
import cn.bupt.tarecruitment.repository.UserRepository;
import cn.bupt.tarecruitment.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Read-only behaviour tests for {@link DefaultAuthService} against the
 * committed seed data in {@code data/users.json}. These tests only invoke
 * {@code login}, which never writes to disk, so the seed data is unchanged.
 */
@DisplayName("DefaultAuthService login against seed data")
class DefaultAuthServiceTest {

    private final AuthService authService = new DefaultAuthService(new UserRepository());

    @Test
    @DisplayName("login with correct seed credentials returns the matching user")
    void loginWithCorrectCredentialsSucceeds() {
        // Act
        Optional<AuthUser> result = authService.login("ta01", "ta01");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ta01", result.get().getUsername());
        assertEquals("TA", result.get().getRole());
    }

    @Test
    @DisplayName("login with a wrong password returns an empty result")
    void loginWithWrongPasswordFails() {
        // Act
        Optional<AuthUser> result = authService.login("ta01", "wrong-password");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("login with an unknown username returns an empty result")
    void loginWithUnknownUserFails() {
        // Act
        Optional<AuthUser> result = authService.login("no-such-user", "whatever");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("login resolves the MO seed account with the MO role")
    void loginResolvesModuleOrganiser() {
        // Act
        Optional<AuthUser> result = authService.login("mo01", "mo01");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("MO", result.get().getRole());
    }

    @Test
    @DisplayName("login resolves the Admin seed account with the ADMIN role")
    void loginResolvesAdmin() {
        // Act
        Optional<AuthUser> result = authService.login("admin01", "admin01");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getRole());
    }
}
