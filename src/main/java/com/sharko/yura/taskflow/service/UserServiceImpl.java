package com.sharko.yura.taskflow.service;

import com.sharko.yura.taskflow.dto.UserCreateDTO;
import com.sharko.yura.taskflow.dto.UserResponseDTO;
import com.sharko.yura.taskflow.dto.UserUpdateDTO;
import com.sharko.yura.taskflow.entity.User;
import com.sharko.yura.taskflow.exception.PasswordMismatchException;
import com.sharko.yura.taskflow.exception.UserAlreadyExistsException;
import com.sharko.yura.taskflow.exception.UserNotFoundException;
import com.sharko.yura.taskflow.exception.UserWithEmailAlreadyExistsException;
import com.sharko.yura.taskflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,  PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public List<UserResponseDTO> findAll() {

        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOS = new ArrayList<>();
        for (User user : users) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUsername(user.getUsername());
            userResponseDTO.setEmail(user.getEmail());
            userResponseDTO.setRole(user.getRole());
            userResponseDTO.setCreatedAt(user.getCreatedAt());
            userResponseDTOS.add(userResponseDTO);
        }

        return userResponseDTOS;

    }

    @Override
    public UserResponseDTO findById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCreatedAt(user.getCreatedAt());

        return userResponseDTO;

    }

    @Override
    @Transactional
    public UserResponseDTO create(UserCreateDTO userCreateDTO) {

        if(!userCreateDTO.isPasswordConfirmed()){
            throw new PasswordMismatchException("Mismatch password");
        }

        if(userRepository.existsByUsername(userCreateDTO.getUsername())){
            throw new UserAlreadyExistsException("User with name " + userCreateDTO.getUsername() + " already exists");
        }

        if(userRepository.existsByEmail(userCreateDTO.getEmail())){
            throw new UserWithEmailAlreadyExistsException("User with email " + userCreateDTO.getEmail() + " already exists");
        }

        String encodedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(userCreateDTO.getRole());

        User saveUser = userRepository.save(user);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(saveUser.getUsername());
        userResponseDTO.setEmail(saveUser.getEmail());
        userResponseDTO.setRole(saveUser.getRole());
        userResponseDTO.setCreatedAt(saveUser.getCreatedAt());

        return userResponseDTO;
    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        if(userRepository.findByUsername(dto.getUsername())!=null){

            throw new UserAlreadyExistsException("User with name " + dto.getUsername() + " already exists");

        }

        if(userRepository.findByEmail(dto.getEmail())!=null){

            throw new UserWithEmailAlreadyExistsException("User with email " + dto.getEmail() + " already exists");

        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        userRepository.save(user);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCreatedAt(user.getCreatedAt());

        return userResponseDTO;

    }

    @Override
    @Transactional
    public void delete(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);

    }

    @Override
    public UserResponseDTO findByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("User with name " + username + " not found");
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCreatedAt(user.getCreatedAt());

        return userResponseDTO;

    }

    @Override
    public UserResponseDTO findByEmail(String email) {

        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new UserNotFoundException("User with email " + email + " not found");
        }
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setCreatedAt(user.getCreatedAt());

        return userResponseDTO;

    }

}
