package com.example.springapp.letyclone.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    Shop findByName(String name);
}
