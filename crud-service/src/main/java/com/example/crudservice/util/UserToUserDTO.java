package com.example.crudservice.util;


import com.example.crudservice.dto.UserDTO;
import com.example.crudservice.model.User;

import java.util.List;

public class UserToUserDTO {

    public static List<UserDTO> convertToUserDTO(List<User> users) {
       return users.stream()
               .map(user -> new UserDTO(user.getName(),user.getEmail(),user.getCreatedAt()))
               .toList();
    }

    public static UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getName(),user.getEmail(),user.getCreatedAt());
    }
}
