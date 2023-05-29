package com.example.shopping_cart.service;

import com.example.shopping_cart.entity.shoppingCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class shopService {

    @Autowired
    private com.example.shopping_cart.repository.shoppingRepository shoppingRepository;

    //Get the car of a client
    public shoppingCar getTheCar(Integer idClient){

        for (shoppingCar sc:shoppingRepository.findAll()){
            if (sc.getId_client()==idClient){
                return sc;
            }
        }
        return null;
    }

    //Get the car
    public shoppingCar gettheCarById(Integer id){
        return shoppingRepository.findById(id).get();
    }

    //Save a new car
    public  int saveCar(shoppingCar shoppingCar){
        //If the car exists it can not be create
        if(shoppingRepository.findById(shoppingCar.getId_client())!=null){
                return -1;
        }
        shoppingRepository.save(shoppingCar);
        return 1;
    }

    //Update car
    public  int updateCar(shoppingCar shoppingCar){
        //If the car exists it can be modify
        if(shoppingRepository.findById(shoppingCar.getId_client())!=null){
        shoppingRepository.save(shoppingCar);
        return 1;
        }
        return -1;
    }













}
