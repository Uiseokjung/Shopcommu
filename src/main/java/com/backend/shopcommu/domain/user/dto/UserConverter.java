package com.backend.shopcommu.domain.user.dto;

import com.backend.shopcommu.domain.user.model.User;

public class UserConverter {

    // DTO를 엔티티로 변환하는 메소드
    public static User dtoToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .phoneNumber(userDTO.getPhoneNumber())
                .role(userDTO.getRole())
                .nickname(userDTO.getNickname())
                .build();
    }

    // 엔티티를 DTO로 변환하는 메소드
    public static UserDTO entityToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .nickname(user.getNickname())
                .build();
    }
}
