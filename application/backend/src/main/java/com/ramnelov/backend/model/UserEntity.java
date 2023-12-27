package com.ramnelov.backend.model;

import com.ramnelov.backend.controller.UserController;
import com.ramnelov.backend.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import com.ramnelov.backend.utils.Role;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static UserEntity fromDTO(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setEmail(userDTO.getEmail().toLowerCase());

        if (userDTO.getRole() != null) {
            userEntity.setRole(Role.valueOf(userDTO.getRole()));
        } else {
            userEntity.setRole(Role.USER);
        }

        logger.info("UserEntity.fromDTO: " + userEntity.toString());

        return userEntity;
    }

    public static UserDTO toDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail().toLowerCase());
        userDTO.setRole(user.getRole().name());

        logger.info("UserEntity.toDTO: " + userDTO.toString());

        return userDTO;

    }

    public UserDetails toSpringSecurityUser() {

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(password)
                .authorities(String.valueOf(role))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();

    }

}
