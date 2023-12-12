package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptEncoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptEncoder);
        User user = new User();
        user.setId(0);
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setCart(new Cart());
        when(userRepo.findById(0L)).thenReturn(java.util.Optional.of(user));
        when(userRepo.findByUsername("testUsername")).thenReturn(user);
    }

    @Test
    public void createUserHappyPath(){
        when(bCryptEncoder.encode("Fahad1234")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Fahad");
        createUserRequest.setPassword("Fahad1234");
        createUserRequest.setConfirmPassword("Fahad1234");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Fahad", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }
    @Test
    public void createUserWithMissMatchPassword(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Fahad");
        createUserRequest.setPassword("Fahad");
        createUserRequest.setConfirmPassword("Fahad1");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserByIdHappyPath(){
        ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
    }
    @Test
    public void findUserByIdDoesNotExist(){
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserByUsernameHappyPath() {
        ResponseEntity<User> response = userController.findByUserName("testUsername");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("testUsername", user.getUsername());
    }
    @Test
    public void findUserByUsernameDoesNotExist() {
        ResponseEntity<User> response = userController.findByUserName("testUsername1");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
