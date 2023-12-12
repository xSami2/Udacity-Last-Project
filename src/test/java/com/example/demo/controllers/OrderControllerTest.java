package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        Item item = getItem();

        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername("Sami");
        user.setCart(cart);

        cart.setId(0L);
        cart.setUser(user);
        cart.setItems(List.of(item));

        UserOrder userOrder = getUserOrder(item, user);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(orderRepository.save(userOrder)).thenReturn(userOrder);
        when(orderRepository.findByUser(user)).thenReturn(List.of(userOrder));
    }

    private static UserOrder getUserOrder(Item item, User user) {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(0L);
        userOrder.setUser(user);
        userOrder.setItems(List.of(item));
        return userOrder;
    }

    private static Item getItem() {
        Item item = new Item();
        item.setId(0L);
        item.setName("Milk");
        BigDecimal price = BigDecimal.valueOf(9.99);
        item.setPrice(price);
        item.setDescription("Fat milk");
        return item;
    }

    @Test
    public void submitHappyPath(){
        ResponseEntity<UserOrder> response = orderController.submit("Sami");
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals("Sami", order.getUser().getUsername());
        assertEquals("Milk", order.getItems().get(0).getName());
    }
    @Test
    public void submitInvalidUser(){
        ResponseEntity<UserOrder> response = orderController.submit("Sami1");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForInvalidUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Sami1");
        assertNotNull(response);
        assertEquals(404,response.getStatusCodeValue());
    }
}
