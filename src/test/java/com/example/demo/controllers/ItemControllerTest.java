package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        Item item = getItem();
        when(itemRepo.findAll()).thenReturn(List.of(item));
        when(itemRepo.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepo.findByName(item.getName())).thenReturn(List.of(item));
    }



    @Test
    public void getItemsHappyPath(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }
    @Test
    public void getItemByIdHappyPath(){
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertNotNull(item);
        Long itemId = item.getId();
        assertEquals(itemId, item.getId());
    }
    @Test
    public void getItemByIdDoesNotExist(){
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemByNameHappyPath(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testName");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items= response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
    }
    @Test
    public void getItemByNameDoesNotExist(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testName1");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    private static Item getItem() {
        Item item = new Item();
        item.setId(0L);
        item.setName("testName");
        BigDecimal itemPrice = BigDecimal.valueOf(9.99);
        item.setPrice(itemPrice);
        item.setDescription("testDescription");
        return item;
    }
}
