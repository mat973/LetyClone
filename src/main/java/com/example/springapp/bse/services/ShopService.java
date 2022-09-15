package com.example.springapp.bse.services;

import com.example.springapp.letyclone.model.Shop;
import com.example.springapp.letyclone.model.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ShopService {
    @Autowired
    ShopRepository shopRepository;
    @Transactional
    public List<Shop> getShops() {
        List<Shop> shops = shopRepository.findAll();
        return shops;
    }
}
