package com.stockmaster.modules.system.service;

import com.stockmaster.modules.system.entity.User;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    User changePassword(Long userId, String newPassword);
}