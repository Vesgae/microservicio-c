package com.example.shopping_cart.entity;


public class servicesShop {


    private Integer availability;
    private Double price;
    private  Integer id_service;

    public servicesShop(Integer availability, Double price, Integer id_service) {
        this.availability = availability;
        this.price = price;
        this.id_service = id_service;
    }

    public servicesShop() {
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getId_service() {
        return id_service;
    }

    public void setId_service(Integer id_service) {
        this.id_service = id_service;
    }

    @Override
    public String toString() {
        return
                "availability=" + availability +
                ", price=" + price +
                ", id_service=" + id_service;
    }
}
