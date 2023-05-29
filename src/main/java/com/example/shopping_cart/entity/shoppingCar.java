package com.example.shopping_cart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="shopping_cart")
public class shoppingCar {

    @Id
    @Column(name = "id_shop")
    private Integer id_shop;

    @Column(name = "total")
    private  Double total;

    @Column(name="id_client")
    private  Integer id_client;

    @Column (name = "pay")
    private  Double pay;

    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    public Integer getId_shop() {
        return id_shop;
    }

    public void setId_shop(Integer id_shop) {
        this.id_shop = id_shop;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getId_client() {
        return id_client;
    }

    public void setId_client(Integer id_client) {
        this.id_client = id_client;
    }


}
