package com.example.bankcards.controller.admin.user;

import com.example.bankcards.dto.user.UserCreateDTO;
import com.example.bankcards.dto.user.UserUpdateDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/user")
@Log4j2
public class AdminUserController {

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserCreateDTO dto) {
        log.info("Received request to create a new user with data : {}", dto.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllUsers() {
        log.info("Received request to get all users");
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllUsersWithPaging(@RequestParam int page, @RequestParam int size) {
        log.info("Received request to get all users with page : {} and size : {}", page, size);
        return getAllUsers();
    }

    @GetMapping
    public ResponseEntity<Void> getAllUsersWithRole(@RequestParam String role) {
        log.info("Received request to get all users with role : {}", role);
        return getAllUsers();
    }

    @GetMapping
    public ResponseEntity<Void> getAllUsersWithRoleAndPaging(
            @RequestParam int page, @RequestParam int size, @RequestParam String role) {
        log.info("Received request to get all users with page : {} and size : {} role : {}", page, size, role);
        return getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getUserById(@PathVariable Long id) {
        log.info("Received request to get User with id : {}", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        log.info("Received request to delete User with id : {}", id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserById(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        log.info("Received request to update User with id : {}", id);
        return ResponseEntity.ok().build();
    }

}
