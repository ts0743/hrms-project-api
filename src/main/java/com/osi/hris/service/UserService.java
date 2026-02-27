package com.osi.hris.service;

import java.util.List;
import com.osi.hris.entity.User;
import com.osi.hris.dto.UserRequestDTO;
import com.osi.hris.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);

    // optional helper
    UserResponseDTO toDTO(User user);
    User fromDTO(UserRequestDTO dto);
}
