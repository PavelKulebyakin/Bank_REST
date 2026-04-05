package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserInfoResponseDTO;
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

    @PostMapping
    public ResponseEntity<UserInfoResponseDTO> createUser(@RequestBody @Valid UserCreateDTO dto) {
        log.info("Received request to create a new user with data : {}", dto.toString());
        UserInfoResponseDTO newUser = adminUserService.createUser(dto);
        return ResponseEntity.created(URI.create("/admin/user/" + newUser.id())).body(newUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInfoResponseDTO>> getAllUsers() {
        log.info("Received request to get all users");
        List<UserInfoResponseDTO> users = adminUserService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping
    public ResponseEntity<List<UserInfoResponseDTO>> getAllUsersWithRoleAndPagination(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String role) {
        log.info("Received request to get all users with page : {} and size : {} role : {}", page, size, role);
        List<UserInfoResponseDTO> users = adminUserService.getAllUsersWithPagingAndRole(page, size, role);
        return  ResponseEntity.ok().body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponseDTO> getUserById(@PathVariable @Min(1) long id) {
        log.info("Received request to get User with id : {}", id);
        UserInfoResponseDTO user = adminUserService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(1) long id) {
        log.info("Received request to delete User with id : {}", id);
        adminUserService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfoResponseDTO> updateUserById(
            @PathVariable @Min(1) long id,
            @RequestBody @Valid  UserUpdateDTO dto) {
        log.info("Received request to update User with id : {}", id);
        UserInfoResponseDTO user = adminUserService.updateUserById(id, dto);
        return ResponseEntity.ok().body(user);
    }

}
