package com.example.shopping_cart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shoppxservice")
public class shopxservice {

    @Id
    @Column(name="id")
    private  Integer id;

    @Column (name = "id_carrito")
    private  Integer id_carrito;

    @Column (name="id_producto")
    private  Integer id_producto;

    @Column(name = "quantiy")
    private Integer quantiy;

    public Integer getQuantiy() {
        return quantiy;
    }

    public void setQuantiy(Integer quantiy) {
        this.quantiy = quantiy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(Integer id_carrito) {
        this.id_carrito = id_carrito;
    }

    public Integer getId_producto() {
        return id_producto;
    }

    public void setId_producto(Integer id_producto) {
        this.id_producto = id_producto;
    }
}
