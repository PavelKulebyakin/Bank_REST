package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.user.UserInfoResponseDTO;
import com.example.bankcards.exception.custom.ResourceAlreadyExistsException;
import com.example.bankcards.exception.custom.ResourceNotFoundException;
import com.example.bankcards.security.CustomUserDetailService;
import com.example.bankcards.security.JwtService;
import com.example.bankcards.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminUserService adminUserService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailService customUserDetailService;

    @Test
    void createUser_success() throws Exception {
        UserInfoResponseDTO response = new UserInfoResponseDTO(1L, "Иван Иванов", "ivanov@mail.com","USER");
        Mockito.when(adminUserService.createUser(any())).thenReturn(response);

        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"ivan@mail.com",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON_PAYLOAD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivanov@mail.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void createUser_nullFullName() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "email":"ivan@mail.com",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").value("Full name is required"));
    }

    @Test
    void createUser_blankFullName() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "fullName":" ",
                    "email":"ivan@mail.com",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").value("Full name is required"));
    }

    @Test
    void createUser_nullEmail() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is required"));
    }

    @Test
    void createUser_blankEmail() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":" ",
                    "password":"pass123"
                }""";
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email should be valid"));
    }

    @Test
    void createUser_invalidEmail() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"abc",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email should be valid"));
    }

    @Test
    void createUser_nullPassword() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"ivan@mail.com"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password is required"));
    }

    @Test
    void createUser_blankPassword() throws Exception {
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"ivan@mail.com",
                    "password":""
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password is required"));
    }

    @Test
    void createUser_nullRole() throws Exception {
        UserInfoResponseDTO response = new UserInfoResponseDTO(1L, "Иван Иванов", "ivanov@mail.ru", "USER");
        Mockito.when(adminUserService.createUser(any())).thenReturn(response);

        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"ivan@mail.com",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void createUser_invalidRole() throws Exception {
        Mockito.when(adminUserService.createUser(any())).thenThrow(new IllegalArgumentException("Role is invalid"));
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"ivan@mail.com",
                    "password":"pass123",
                    "role":"MANAGER"
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_emptyBody() throws Exception {
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fullName").value("Full name is required"))
                .andExpect(jsonPath("$.email").value("Email is required"))
                .andExpect(jsonPath("$.password").value("Password is required"));

        Mockito.verify(adminUserService, Mockito.never()).createUser(any());
    }

    @Test
    void createUser_invalidJson() throws Exception {
        String JSON_PAYLOAD = """
                {
                    fullName: "Ivan",
                    email
                }
                """;
        mockMvc.perform(post("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));

        Mockito.verify(adminUserService, Mockito.never()).createUser(any());
    }

    @Test
    void createUser_emailAlreadyExists() throws Exception {
        Mockito.when(adminUserService.createUser(any())).thenThrow(new ResourceAlreadyExistsException("Email already exists"));
        String JSON_PAYLOAD = """
                {
                    "fullName":"Иван Иванов",
                    "email":"ivan@mail.com",
                    "password":"pass123"
                }
                """;
        mockMvc.perform(post("/admin/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSON_PAYLOAD))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    void getAllUsers_success() throws Exception {
        List<UserInfoResponseDTO> users = List.of(
                new UserInfoResponseDTO(1L, "Иван Иванов", "ivan@mail.com", "USER"),
                new UserInfoResponseDTO(2L, "Петр Петров", "petr@mail.com", "ADMIN")
        );

        Mockito.when(adminUserService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/admin/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$[0].email").value("ivan@mail.com"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].fullName").value("Петр Петров"))
                .andExpect(jsonPath("$[1].email").value("petr@mail.com"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));

        Mockito.verify(adminUserService).getAllUsers();
    }

    @Test
    void getAllUsers_emptyList() throws Exception {
        Mockito.when(adminUserService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        Mockito.verify(adminUserService).getAllUsers();
    }

    @Test
    void getAllUsersWithRoleAndPagination_success() throws Exception {
        List<UserInfoResponseDTO> users = List.of(
                new UserInfoResponseDTO(1L, "Иван Иванов", "ivan@mail.com", "USER")
        );

        Mockito.when(adminUserService.getAllUsersWithPagingAndRole(1, 15, "USER")).thenReturn(users);

        mockMvc.perform(get("/admin/user")
                        .param("page", "1")
                        .param("size", "15")
                        .param("role", "USER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(adminUserService).getAllUsersWithPagingAndRole(1, 15, "USER");

    }

    @Test
    void getAllUsersWithRoleAndPagination_nullPage() throws Exception {
        mockMvc.perform(get("/admin/user")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(adminUserService).getAllUsersWithPagingAndRole(1, 10, null);
    }

    @Test
    void getAllUsersWithRoleAndPagination_invalidPage() throws Exception {
        mockMvc.perform(get("/admin/user")
                        .param("page", "-1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.page").value("must be greater than or equal to 1"));
    }

    @Test
    void getAllUsersWithRoleAndPagination_nullSize() throws Exception {
        mockMvc.perform(get("/admin/user")
                        .param("page", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(adminUserService).getAllUsersWithPagingAndRole(2, 10, null);
    }

    @Test
    void getAllUsersWithRoleAndPagination_invalidSize() throws Exception {
        mockMvc.perform(get("/admin/user")
                        .param("page", "1")
                        .param("size", "-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.size").value("must be greater than or equal to 1"));
    }

    @Test
    void getAllUsersWithRoleAndPagination_typeMismatch() throws Exception {
        mockMvc.perform(get("/admin/user")
                        .param("page", "a")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.page").exists());
    }

    @Test
    void getAllUsersWithRoleAndPaging_successWithNullRole() throws Exception {
        List<UserInfoResponseDTO> users = List.of(
                new UserInfoResponseDTO(1L, "Иван Иванов", "ivan@mail.com", "USER")
        );

        Mockito.when(adminUserService.getAllUsersWithPagingAndRole(1, 10, null)).thenReturn(users);

        mockMvc.perform(get("/admin/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(adminUserService).getAllUsersWithPagingAndRole(1, 10, null);
    }

    @Test
    void getAllUsersWithRoleAndPaging_invalidRole() throws Exception {
        Mockito.when(adminUserService.getAllUsersWithPagingAndRole(
                        anyInt(),
                        anyInt(),
                        eq("INVALID")))
                .thenThrow(new IllegalArgumentException("Role is invalid"));

        mockMvc.perform(get("/admin/user")
                        .param("page", "1")
                        .param("size", "10")
                        .param("role", "INVALID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_success() throws Exception {
        UserInfoResponseDTO user = new UserInfoResponseDTO(1L, "Иван Иванов", "ivan@mail.com", "USER");
        Mockito.when(adminUserService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/admin/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivan@mail.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        Mockito.verify(adminUserService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_invalidId() throws Exception {
        mockMvc.perform(get("/admin/user/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void getUserById_notFound() throws Exception {
        Mockito.when(adminUserService.getUserById(999L))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get("/admin/user/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void getUserById_typeMismatch() throws Exception {
        mockMvc.perform(get("/admin/user/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deleteUserById_success() throws Exception {
        mockMvc.perform(delete("/admin/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(adminUserService, times(1)).deleteUserById(1L);
    }

    @Test
    void deleteUserById_invalidId() throws Exception {
        mockMvc.perform(delete("/admin/user/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void deleteUserById_notFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found"))
                .when(adminUserService).deleteUserById(999L);

        mockMvc.perform(delete("/admin/user/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void deleteUserById_typeMismatch() throws Exception {
        mockMvc.perform(delete("/admin/user/{id}", "abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void updateUserById_success() throws Exception {
        UserInfoResponseDTO updatedUser = new UserInfoResponseDTO(1L, "John Doe", "john@example.com", "ADMIN");
        String JSON_PAYLOAD = """
        {
            "fullName": "John Doe",
            "email": "john@example.com",
            "password": "newpass",
            "role": "ADMIN"
        }
        """;

        Mockito.when(adminUserService.updateUserById(eq(1L), Mockito.any()))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/admin/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_PAYLOAD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        Mockito.verify(adminUserService, times(1)).updateUserById(Mockito.eq(1L), Mockito.any());
    }

    @Test
    void updateUserById_invalidEmail() throws Exception {
        String invalidEmailPayload = """
            {
                "fullName": "John Doe",
                "email": "not-an-email",
                "password": "newpass",
                "role": "ADMIN"
            }
            """;

        mockMvc.perform(put("/admin/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidEmailPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

}
