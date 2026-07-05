package com.food.menu.service;

import com.food.menu.entity.Menu;
import com.food.menu.repository.MenuRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository repo;

    @InjectMocks
    private MenuService service;

    @Test
    void addMenu() {

        Menu menu = new Menu();

        menu.setRestaurantId(1L);
        menu.setName("Veg Pizza");
        menu.setPrice(299.0);

        when(repo.save(menu))
                .thenReturn(menu);

        Menu saved =
                service.add(menu);

        assertNotNull(saved);

        assertEquals("Veg Pizza",
                saved.getName());

        assertEquals(299.0,
                saved.getPrice());
    }

    @Test
    void getByRestaurant() {

        Menu menu = new Menu();

        menu.setRestaurantId(1L);
        menu.setName("Burger");

        when(repo.findByRestaurantId(1L))
                .thenReturn(List.of(menu));

        List<Menu> menus =
                service.getByRestaurant(1L);

        assertEquals(1,
                menus.size());

        assertEquals("Burger",
                menus.get(0).getName());
    }

    @Test
    void getAllMenus() {

        when(repo.findAll())
                .thenReturn(List.of(
                        new Menu(),
                        new Menu()
                ));

        List<Menu> menus =
                service.getAll();

        assertEquals(2,
                menus.size());
    }
}