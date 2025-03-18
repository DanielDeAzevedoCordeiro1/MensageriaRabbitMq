package com.example.crudservice.service.user;



import com.example.crudservice.dto.UserDTO;
import com.example.crudservice.model.User;
import com.example.crudservice.model.repository.UserRepository;
import com.example.crudservice.util.UserToUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreateUserService {

    private final UserRepository userRepository;

    public CreateUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        var user = userRepository.findAll();
        return UserToUserDTO.convertToUserDTO(user);
    }
}
