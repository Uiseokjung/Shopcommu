package com.backend.shopcommu.domain.user.service;

import com.backend.shopcommu.domain.user.dto.UserDTO;
import com.backend.shopcommu.domain.user.model.User;
import com.backend.shopcommu.domain.user.repository.UserRepository;
import com.backend.shopcommu.domain.user.dto.UserConverter;
import com.backend.shopcommu.global.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        User user = UserConverter.dtoToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return UserConverter.entityToDto(savedUser);
    }

    // 로그인
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // JWT 토큰 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        claims.put("nickname", user.getNickname());

        return jwtUtil.generateToken(claims, 3600); // 1시간(3600초) 만료
    }

    // 사용자 정보 수정
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // 비밀번호 암호화
        user.setRole(userDTO.getRole());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setNickname(userDTO.getNickname());

        User updatedUser = userRepository.save(user);
        return UserConverter.entityToDto(updatedUser);
    }

    // 사용자 삭제
    @Transactional
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // 사용자 조회
    public Optional<UserDTO> findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(UserConverter::entityToDto);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(UserConverter::entityToDto);
    }

    public Optional<UserDTO> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname).map(UserConverter::entityToDto);
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id).map(UserConverter::entityToDto);
    }
}
