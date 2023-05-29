package com.example.shopping_cart.repository;

import com.example.shopping_cart.entity.shopxservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface shopXserRepository extends JpaRepository<shopxservice,Integer> {



}
