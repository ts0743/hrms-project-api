package com.osi.hris.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.osi.hris.entity.User;
import com.osi.hris.dto.UserRequestDTO;
import com.osi.hris.dto.UserResponseDTO;
import com.osi.hris.repository.UserRepository;
import com.osi.hris.service.UserService;
import com.osi.hris.exception.ConflictException; // ✅ ADD THIS

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        logger.info("Creating user with email={}", dto.getEmail());

        Optional<User> existing = userRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            // ✅ ONLY CHANGE: RuntimeException → ConflictException
            throw new ConflictException(
                "User already exists with email=" + dto.getEmail()
            );
        }

        User user = fromDTO(dto);
        userRepository.save(user);
        return toDTO(user);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user with id={}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with id={}", id);
        userRepository.deleteById(id);
    }

    // Helper to convert entity to DTO
    @Override
    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    // Helper to convert DTO to entity
    @Override
    public User fromDTO(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole("USER"); // default role
        return user;
    }
}
