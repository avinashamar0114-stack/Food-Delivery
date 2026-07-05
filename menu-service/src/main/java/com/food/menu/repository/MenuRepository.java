package com.food.menu.repository;

import com.food.menu.entity.Menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository
        extends JpaRepository<Menu, Long> {

    List<Menu> findByRestaurantId(
            Long restaurantId
    );

    Optional<Menu> findByRestaurantIdAndNameIgnoreCase(
            Long restaurantId,
            String name
    );
}