package com.food.menu.controller;

import com.food.menu.entity.Menu;
import com.food.menu.repository.MenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")

@CrossOrigin(
    origins = "http://localhost:4200",
    allowCredentials = "true"
)

public class MenuController {

    @Autowired
    private MenuRepository menuRepository;

    @PostMapping("/add")
    public Object add(
            @RequestBody Menu menu
    ) {

        System.out.println("MENU RECEIVED = " + menu);

        // CHECK DUPLICATE
        boolean exists = menuRepository
                .findByRestaurantIdAndNameIgnoreCase(
                        menu.getRestaurantId(),
                        menu.getName()
                )
                .isPresent();

        if (exists) {

            return "Dish already exists for this restaurant";
        }

        return menuRepository.save(menu);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Menu> getByRestaurant(
            @PathVariable Long restaurantId
    ) {

        System.out.println(
            "FETCHING MENU FOR RESTAURANT: "
            + restaurantId
        );

        return menuRepository
                .findByRestaurantId(restaurantId);
    }

    @GetMapping
    public List<Menu> getAll() {

        return menuRepository.findAll();
    }
}