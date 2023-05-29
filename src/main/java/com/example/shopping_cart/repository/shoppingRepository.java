package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.shoppingCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface shoppingRepository  extends JpaRepository<shoppingCar, Integer> {

}
