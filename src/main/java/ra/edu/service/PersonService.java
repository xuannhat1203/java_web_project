package ra.edu.service;

import ra.edu.entity.User;

import java.util.List;

public interface PersonService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void register(User user);
    User login(String username, String password);
    boolean updatePassword(int id, String newPassword);
    boolean updateProfile(User user);
    List<User> getAllUsers();
}
