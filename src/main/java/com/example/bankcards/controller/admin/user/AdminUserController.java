package com.example.bankcards.controller.admin.user;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserResponseDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;
import com.example.bankcards.service.AdminUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Log4j2
public class AdminUserController {

    private final AdminUserService adminUserService;

    // TODO add role validation
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
        log.info("Received request to create a new user with data : {}", dto.toString());
        UserResponseDTO newUser = adminUserService.createUser(dto);
        return ResponseEntity.created(URI.create("/admin/user/" + newUser.getId())).body(newUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("Received request to get all users");
        List<UserResponseDTO> users = adminUserService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    // TODO add paging + filter
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsersWithRoleAndPaging(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String role) {
        log.info("Received request to get all users with page : {} and size : {} role : {}", page, size, role);
        List<UserResponseDTO> users = adminUserService.getAllUsersWithPagingAndRole(page, size, role);
        return  ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable @Min(1) Long id) {
        log.info("Received request to get User with id : {}", id);
        UserResponseDTO user = adminUserService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(1) Long id) {
        log.info("Received request to delete User with id : {}", id);
        adminUserService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserById(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody UserUpdateDTO dto) {
        log.info("Received request to update User with id : {}", id);
        UserResponseDTO user = adminUserService.updateUserById(id, dto);
        return ResponseEntity.ok().body(user);
    }

}
