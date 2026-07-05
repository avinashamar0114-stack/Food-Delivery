package com.food.menu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.menu.entity.Menu;
import com.food.menu.repository.MenuRepository;

@Service
public class MenuService {

    @Autowired
    private MenuRepository repo;

    public Menu add(Menu m) {
        return repo.save(m);
    }

    public List<Menu> getByRestaurant(Long restaurantId) {
        return repo.findByRestaurantId(restaurantId);
    }

    public List<Menu> getAll() {
        return repo.findAll();
    }
}
