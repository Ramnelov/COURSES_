package com.ramnelov.backend.controller;

import com.ramnelov.backend.dto.UserDTO;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.service.TokenService;
import com.ramnelov.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;


    public UserController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/admin")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        try {
            userService.getUserByUsername(userDTO.getUsername());

            // If the user is found, return a response indicating that the user already exists
            return ResponseEntity.status(401).body("User already exists");
        } catch (RuntimeException e) {
            // If the exception is thrown, it means the user was not found, so proceed with registration
            userService.createUser(UserEntity.fromDTO(userDTO));
            return ResponseEntity.ok("Registration successful");
        }
    }

    @PostMapping("/token")
    public String token(@RequestBody UserDTO userDTO) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        return tokenService.generateToken(authentication);
    }

    @PutMapping("admin/{id}")
    public ResponseEntity<String> updateUserAdmin(@PathVariable Long id, @RequestBody UserDTO userDTO, @RequestAttribute("jwt") Jwt jwt) {
        try {
            UserEntity user = userService.getUserById(id).orElseThrow(RuntimeException::new);


            if (tokenService.tokenAuthority(jwt).equals("ADMIN")) {
                if (userDTO.getUsername() != null) {
                    user.setUsername(userDTO.getUsername());
                } if (userDTO.getPassword() != null) {
                    if (userService.isPasswordSafe(userDTO.getPassword())) {
                        user.setPassword(userDTO.getPassword());
                    } else {
                        return ResponseEntity.status(401).body("Password not safe");
                    }
                } if (userDTO.getRole() != null) {
                    user.setRole(userDTO.getRole());
                }
            } else {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            userService.updateUser(user);

            return ResponseEntity.ok("User updated");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("User not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO, @RequestAttribute("jwt") Jwt jwt) {
        try {
            UserEntity user = userService.getUserById(id).orElseThrow(RuntimeException::new);


            if (user.getUsername().equals(tokenService.tokenUsername(jwt))) {
                if (userDTO.getUsername() != null) {
                    user.setUsername(userDTO.getUsername());
                } if (userDTO.getPassword() != null) {
                    if (userService.isPasswordSafe(userDTO.getPassword())) {
                        user.setPassword(userDTO.getPassword());
                    } else {
                        return ResponseEntity.status(401).body("Password not safe");
                    }
                }
            }  else {
                return ResponseEntity.status(401).body("Unauthorized");
            }

            userService.updateUser(user);

            return ResponseEntity.ok("User updated");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("User not found");
        }
    }
}
