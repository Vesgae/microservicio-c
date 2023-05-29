package com.example.shopping_cart.service;

import com.example.shopping_cart.entity.shopxservice;
import com.example.shopping_cart.repository.shopXserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class shopXserviceService {
    @Autowired
    private shopXserRepository shopxserRepository;

    //Find the products in the car of a client
    public List<shopxservice> getAllProducts(Integer idCar){
        List<shopxservice> services= new ArrayList<>();
        for (shopxservice shop: shopxserRepository.findAll()){
            if(idCar==shop.getId_carrito()){
                services.add(shop);
            }
        }
        return services;
    }
    
    //Add products to the car 
    public int addProducts(shopxservice shopxservice){
        //If the id exist already
        for (shopxservice shop : shopxserRepository.findAll()) {
            if (shop.getId() == shopxservice.getId()) {
                return -1;
            }
        }
            //If the product exists
        for (shopxservice shop : shopxserRepository.findAll()) {
            if (shop.getId_producto() == shopxservice.getId_producto() && shopxservice.getId_carrito()==shop.getQuantiy()) {
                    return 0;
                }
            }

            //If it has not been found, add
            shopxserRepository.save(shopxservice);
            return 1;
    }

    //Update product
    public int updateProducts(shopxservice shopxservice) {
        //The product does not exist
        if (shopxserRepository.findById(shopxservice.getId()).get() == null) {
            return -1;
        }
        shopxservice auxShopxservice = shopxserRepository.findById(shopxservice.getId()).get();
        auxShopxservice.setQuantiy(shopxservice.getQuantiy());
        shopxserRepository.save(auxShopxservice);
        return 1;
    }

    //Delete product from de car
    public int deleteProduct(Integer idProduct, Integer idCar){
        for (shopxservice shop: shopxserRepository.findAll()){
            if (shop.getId_producto()==idProduct && idCar==shop.getId_carrito()){
                shopxserRepository.deleteById(shop.getId());
                return 1;
            }
        }
        return -1;
    }

    //Empty the car
    public void deleteAllProducts(Integer idCar){
        for (shopxservice shop: shopxserRepository.findAll()){
            if (idCar==shop.getId_carrito()){
                shopxserRepository.deleteById(shop.getId());
            }
        }

    }

}
