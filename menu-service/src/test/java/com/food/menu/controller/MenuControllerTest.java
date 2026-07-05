package com.food.menu.controller;

import com.food.menu.entity.Menu;
import com.food.menu.repository.MenuRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

    @Mock
    private MenuRepository repo;

    @InjectMocks
    private MenuController controller;

    @Test
    void addMenu() {

        Menu menu = new Menu();

        menu.setRestaurantId(1L);
        menu.setName("Pizza");
        menu.setPrice(250.0);

        when(repo.findByRestaurantIdAndNameIgnoreCase(
                1L,
                "Pizza"
        )).thenReturn(Optional.empty());

        when(repo.save(any(Menu.class)))
                .thenReturn(menu);

        Object response =
                controller.add(menu);

        assertTrue(response instanceof Menu);

        Menu saved = (Menu) response;

        assertEquals(
                "Pizza",
                saved.getName()
        );
    }

    @Test
    void duplicateMenu() {

        Menu menu = new Menu();

        menu.setRestaurantId(1L);
        menu.setName("Pizza");

        when(repo.findByRestaurantIdAndNameIgnoreCase(
                1L,
                "Pizza"
        )).thenReturn(Optional.of(menu));

        Object response =
                controller.add(menu);

        assertEquals(
                "Dish already exists for this restaurant",
                response
        );
    }

    @Test
    void getByRestaurant() {

        Menu menu = new Menu();

        menu.setRestaurantId(1L);

        when(repo.findByRestaurantId(1L))
                .thenReturn(List.of(menu));

        List<Menu> menus =
                controller.getByRestaurant(1L);

        assertEquals(
                1,
                menus.size()
        );
    }

    @Test
    void getAllMenus() {

        when(repo.findAll())
                .thenReturn(List.of(
                        new Menu(),
                        new Menu()
                ));

        List<Menu> menus =
                controller.getAll();

        assertEquals(
                2,
                menus.size()
        );
    }
}