package com.ramnelov.backend.controller;

import com.ramnelov.backend.dto.UserDTO;
import com.ramnelov.backend.model.UserEntity;
import com.ramnelov.backend.service.TokenService;
import com.ramnelov.backend.service.UserService;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.ramnelov.backend.utils.Role;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final Bucket bucket;


    public UserController(UserService userService, TokenService tokenService, AuthenticationManager authenticationManager, Bucket bucket) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.bucket = bucket;
    }

    @PostMapping("/")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        if (bucket.tryConsume(1)) {
            if (userService.userExistsByUsername(userDTO.getUsername())) {
                logger.error("User already exists: " + userDTO.getUsername());
                return ResponseEntity.status(401).body("User already exists");

            } else if (userService.userExistsByEmail(userDTO.getEmail())) {
                logger.error("Email already exists: " + userDTO.getEmail());
                return ResponseEntity.status(401).body("Email already exists");

            } else {

                if (!userService.isValidUsername(userDTO.getUsername())) {
                    logger.error("Invalid username: " + userDTO.getUsername());
                    return ResponseEntity.status(401).body("Invalid username");
                }

                if (!userService.isValidEmail(userDTO.getEmail())) {
                    logger.error("Invalid email: " + userDTO.getEmail());
                    return ResponseEntity.status(401).body("Invalid email");
                }

                if (!userService.isPasswordSafe(userDTO.getPassword())) {
                    logger.error("Password not safe: " + userDTO.getPassword());
                    return ResponseEntity.status(401).body("Password not safe");
                }

                UserEntity user = UserEntity.fromDTO(userDTO);
                userService.createUser(user);

                logger.info("Registration successful: " + user);
                return ResponseEntity.ok("Registration successful");
            }

        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }
    }

    @PostMapping("/admin")
    public ResponseEntity<String> createUserAdmin(@RequestBody UserDTO userDTO, @RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {
            if (tokenService.getAuthority(jwt).equals("ADMIN")) {

                if (userService.userExistsByUsername(userDTO.getUsername())) {
                    logger.error("User already exists: " + userDTO.getUsername());
                    return ResponseEntity.status(401).body("User already exists");

                } else if (userService.userExistsByEmail(userDTO.getEmail())) {
                    logger.error("Email already exists: " + userDTO.getEmail());
                    return ResponseEntity.status(401).body("Email already exists");

                } else {

                    if (!userService.isValidUsername(userDTO.getUsername())) {
                        logger.error("Invalid username: " + userDTO.getUsername());
                        return ResponseEntity.status(401).body("Invalid username");
                    }

                    if (!userService.isValidEmail(userDTO.getEmail())) {
                        logger.error("Invalid email: " + userDTO.getEmail());
                        return ResponseEntity.status(401).body("Invalid email");
                    }

                    if (!userService.isPasswordSafe(userDTO.getPassword())) {
                        logger.error("Password not safe: " + userDTO.getPassword());
                        return ResponseEntity.status(401).body("Password not safe");
                    }

                    if (!userService.isValidRole(userDTO.getRole())) {
                        logger.error("Invalid : " + userDTO.getRole());
                        return ResponseEntity.status(401).body("Invalid role");
                    }

                    UserEntity user = UserEntity.fromDTO(userDTO);
                    userService.createUser(user);

                    logger.info("Registration successful");
                    return ResponseEntity.ok("Registration successful: " + user);
                }
            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body("Unauthorized");
            }


        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }

    }


    @PostMapping("/token")
    public ResponseEntity<String> token(@RequestBody UserDTO userDTO) throws AuthenticationException {
        if (bucket.tryConsume(1)) {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            String token = tokenService.generateToken(authentication);

            logger.info("Token generated: " + token);

            return ResponseEntity.ok(token);

        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }
    }

    @PutMapping("admin/{id}")
    public ResponseEntity<String> updateUserAdmin(@PathVariable Long id, @RequestBody UserDTO userDTO, @RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {
            if (tokenService.getAuthority(jwt).equals("ADMIN")) {

                if (userService.userExistById(id)) {
                    UserEntity user = userService.getUserById(id).orElseThrow(RuntimeException::new);

                    if (userDTO.getUsername() != null && !userDTO.getUsername().equalsIgnoreCase(user.getUsername())) {

                        if (!userService.isValidUsername(userDTO.getUsername())) {
                            logger.error("Invalid username: " + userDTO.getUsername() + "for user: " + user);
                            return ResponseEntity.status(401).body("Invalid username");

                        } else if (userService.userExistsByUsername(userDTO.getUsername())) {
                            logger.error("User already exists: " + userDTO.getUsername() + "for user: " + user);
                            return ResponseEntity.status(401).body("User already exists");

                        } else {
                            user.setUsername(userDTO.getUsername());
                        }
                    }

                    if (userDTO.getPassword() != null && !user.getPassword().equals(userService.encodePassword(userDTO.getPassword()))) {

                        if (userService.isPasswordSafe(userDTO.getPassword())) {
                            user.setPassword(userDTO.getPassword());

                        } else {
                            logger.error("Password not safe: " + userDTO.getPassword() + "for user: " + user);
                            return ResponseEntity.status(401).body("Password not safe");
                        }
                    }

                    if (userDTO.getEmail() != null && !userDTO.getEmail().equalsIgnoreCase(user.getEmail())) {
                        if (!userService.isValidEmail(userDTO.getEmail())) {
                            logger.error("Invalid email: " + userDTO.getEmail() + "for user: " + user);
                            return ResponseEntity.status(401).body("Invalid email");

                        } else if (userService.userExistsByEmail(userDTO.getEmail())) {
                            logger.error("Email already exists: " + userDTO.getEmail() + "for user: " + user);
                            return ResponseEntity.status(401).body("Email already exists");

                        } else {
                            user.setEmail(userDTO.getEmail().toLowerCase());
                        }
                    }

                    if (userDTO.getRole() != null && !userDTO.getRole().equals(user.getRole().name())) {

                        if (userService.isValidRole(userDTO.getRole())) {
                            user.setRole(Role.valueOf(userDTO.getRole()));
                        } else {
                            logger.error("Invalid role: " + userDTO.getRole() + "for user: " + user);
                            return ResponseEntity.status(401).body("Invalid role");
                        }
                    }


                    userService.updateUser(user);
                    logger.info("User updated: " + user);
                    return ResponseEntity.ok("User updated");
                } else {
                    logger.error("User not found: " + tokenService.getUsername(jwt));
                    return ResponseEntity.status(401).body("User not found");
                }
            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }
    }

    @PutMapping("/")
    public ResponseEntity<String> updateUser(@RequestBody UserDTO userDTO, @RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {

            if (userService.userExistsByUsername(tokenService.getUsername(jwt))) {
                UserEntity user = userService.getUserByUsername(tokenService.getUsername(jwt));

                if (userDTO.getUsername() != null && !userDTO.getUsername().equalsIgnoreCase(user.getUsername())) {
                    if (!userService.isValidUsername(userDTO.getUsername())) {
                        logger.error("Invalid username: " + userDTO.getUsername() + "for user: " + user);
                        return ResponseEntity.status(401).body("Invalid username");
                    } else if (userService.userExistsByUsername(userDTO.getUsername())) {
                        logger.error("User already exists: " + userDTO.getUsername() + "for user: " + user);
                        return ResponseEntity.status(401).body("User already exists");
                    } else {
                        user.setUsername(userDTO.getUsername());
                    }
                }

                if (userDTO.getPassword() != null && !user.getPassword().equals(userService.encodePassword(userDTO.getPassword()))) {
                    if (userService.isPasswordSafe(userDTO.getPassword())) {
                        user.setPassword(userDTO.getPassword());
                    } else {
                        logger.error("Password not safe: " + userDTO.getPassword() + "for user: " + user);
                        return ResponseEntity.status(401).body("Password not safe");
                    }
                }

                if (userDTO.getEmail() != null && !userDTO.getEmail().equalsIgnoreCase(user.getEmail())) {
                    if (!userService.isValidEmail(userDTO.getEmail())) {
                        logger.error("Invalid email: " + userDTO.getEmail() + "for user: " + user);
                        return ResponseEntity.status(401).body("Invalid email");
                    } else if (userService.userExistsByEmail(userDTO.getEmail())) {
                        logger.error("Email already exists: " + userDTO.getEmail() + "for user: " + user);
                        return ResponseEntity.status(401).body("Email already exists");
                    } else {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                }

                userService.updateUser(user);
                logger.info("User updated: " + user);

                return ResponseEntity.ok("User updated");
            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }

    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteUser(@RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {
            if (userService.userExistsByUsername(tokenService.getUsername(jwt))) {

                UserEntity user = userService.getUserByUsername(tokenService.getUsername(jwt));


                userService.deleteUser(user.getId());
                logger.info("User deleted: " + user);
                return ResponseEntity.ok("User deleted");

            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteUserAdmin(@PathVariable Long id, @RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {
            if (tokenService.getAuthority(jwt).equals("ADMIN")) {
                if (userService.userExistById(id)) {
                    UserEntity user = userService.getUserById(id).orElseThrow(RuntimeException::new);
                    userService.deleteUser(id);
                    logger.info("User deleted: " + user);
                    return ResponseEntity.ok("User deleted");
                } else {
                    logger.error("User not found: " + tokenService.getUsername(jwt));
                    return ResponseEntity.status(401).body("User not found: " + tokenService.getUsername(jwt));
                }
            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body("Unauthorized");
            }
        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getUser(@RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {
            if (userService.userExistsByUsername(tokenService.getUsername(jwt))) {
                UserEntity user = userService.getUserByUsername(tokenService.getUsername(jwt));
                logger.info("User found: " + user);
                return ResponseEntity.ok(UserEntity.toDTO(user));
            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body(null);
            }
        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }


    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDTO>> getUserAdmin(@RequestAttribute("jwt") Jwt jwt) {
        if (bucket.tryConsume(1)) {
            if (tokenService.getAuthority(jwt).equals("ADMIN")) {
                List<UserEntity> users = userService.getAllUsers();
                List<UserDTO> userDTOs = users.stream().map(UserEntity::toDTO).collect(Collectors.toList());
                logger.info("Users found: " + users);
                return ResponseEntity.ok(userDTOs);
            } else {
                logger.error("Unauthorized");
                return ResponseEntity.status(401).body(null);
            }
        } else {
            logger.error("Too many requests");
            return ResponseEntity.status(429).body(null);
        }


    }
}
