package ra.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.entity.User;
import ra.edu.repository.PersonRepository;

@Service
public class PersonServiceImp implements PersonService {
    @Autowired
    private PersonRepository authRepository;

    @Override
    public boolean existsByUsername(String username) {
        return authRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return authRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return authRepository.existsByPhone(phone);
    }

    @Override
    public void register(User user) {
        authRepository.register(user);
    }

    @Override
    public User login(String username, String password) {
        return authRepository.login(username, password);
    }

}