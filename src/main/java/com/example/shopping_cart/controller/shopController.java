package com.example.shopping_cart.controller;


import com.example.shopping_cart.entity.servicesShop;
import com.example.shopping_cart.entity.shoppingCar;
import com.example.shopping_cart.entity.shopxservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/shopping")
//http://localhost:8084/shopping/
public class shopController {

    @Autowired
    private com.example.shopping_cart.service.shopService shopService;

    @Autowired
    private com.example.shopping_cart.service.shopXserviceService shopXserviceService;

    //Get a car
    @GetMapping("/car/{idClient}")
    public ResponseEntity<shoppingCar> getCar(@PathVariable Integer idClient) {
        if (shopService.getTheCar(idClient) != null) {
            return new ResponseEntity<>(shopService.getTheCar(idClient), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    //Create a car
    @PostMapping("/car")
    public ResponseEntity<String> createCar(@RequestBody shoppingCar shoppingCar) {
        if (shopService.saveCar(shoppingCar) == 1) {
            return new ResponseEntity<>("Se ha creado el carro correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ya existe este carrito de compras. Agregue o elimine productos", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/car")
    public ResponseEntity<String> updateCar(@RequestBody shoppingCar shoppingCar) {
        if (shopService.updateCar(shoppingCar) == 1) {
            return new ResponseEntity<>("Se ha moddificado el carro correctamente", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No se pudo modificar", HttpStatus.BAD_REQUEST);
        }
    }

    //Get the products of the car
    @GetMapping("/products/{idClient}")
    public ResponseEntity<List<shopxservice>> getProducts(@PathVariable Integer idClient) {
        shoppingCar shoppingCarAux = shopService.getTheCar(idClient);
        if (shopXserviceService.getAllProducts(shoppingCarAux.getId_shop()).size() == 0) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else if (shoppingCarAux == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(shopXserviceService.getAllProducts(shoppingCarAux.getId_shop()), HttpStatus.OK);
        }
    }

    //Add products to the car
    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody shopxservice shopxservice) {
        List<servicesShop> auxShopping = getServices();
        if (checkAvailability(auxShopping, shopxservice.getQuantiy(), shopxservice.getId_producto()) == -1) {
            return new ResponseEntity<>("La cantidad solicitada no esta disponible por el momento", HttpStatus.BAD_REQUEST);
        }else if (shopXserviceService.addProducts(shopxservice) == -1) {
            return new ResponseEntity<>("Ya existe el id", HttpStatus.BAD_REQUEST);
        } else if (shopXserviceService.addProducts(shopxservice) == 0) {
            return new ResponseEntity<>("El producto ya se ha agregado, intente modificarlo en su lugar", HttpStatus.BAD_REQUEST);
        } else {
            shoppingCar shoppingCarAux = shopService.gettheCarById(shopxservice.getId_carrito());
            shoppingCarAux.setTotal(getTotalShop(auxShopping, shopxservice.getId_carrito()));
            shopService.updateCar(shoppingCarAux);
            return new ResponseEntity<>("Se ha agregado al carro correctamente", HttpStatus.OK);
        }
    }

    //Modify product
    @PutMapping("/product")
    public ResponseEntity<String> updateProduct(@RequestBody shopxservice shopxservice) {
        List<servicesShop> auxShopping = getServices();
        if (checkAvailability(auxShopping, shopxservice.getQuantiy(), shopxservice.getId_producto()) == -1) {
            return new ResponseEntity<>("La cantidad solicitada no esta disponible por el momento", HttpStatus.BAD_REQUEST);
        }else if (shopXserviceService.updateProducts(shopxservice) == -1) {
            return new ResponseEntity<>("No existe el producto en el carro", HttpStatus.BAD_REQUEST);
        }else {
            shoppingCar shoppingCarAux = shopService.gettheCarById(shopxservice.getId_carrito());
            shoppingCarAux.setTotal(getTotalShop(auxShopping, shopxservice.getId_carrito()));
            shopService.updateCar(shoppingCarAux);
            return new ResponseEntity<>("Se ha modificado el producto correctamente", HttpStatus.OK);
        }
    }

    //Delete product
    @DeleteMapping("/product/{idProduct}/{idClient}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer idProduct, @PathVariable Integer idClient) {
        shoppingCar shoppingCar = shopService.getTheCar(idClient);
        if (shopXserviceService.deleteProduct(idProduct, shoppingCar.getId_shop()) == -1) {
            return new ResponseEntity<>("No existe el producto en el carro", HttpStatus.BAD_REQUEST);
        } else {
            List<servicesShop> auxShopping = getServices();
            shoppingCar.setTotal(getTotalShop(auxShopping, shoppingCar.getId_shop()));
            shopService.updateCar(shoppingCar);
            return new ResponseEntity<>("Se ha eliminado el producto correctamente", HttpStatus.OK);
        }
    }

    //Payment Part
    @GetMapping("/payment/{value}/{idClient}")
    public ResponseEntity<String> paymentClient(@PathVariable Double value, @PathVariable Integer idClient) {
        shoppingCar shoppingCar = shopService.getTheCar(idClient);

        if ((shoppingCar.getTotal() - value)!=0) {
            return new ResponseEntity<>("Se tiene que pagar completo", HttpStatus.BAD_REQUEST);
        } else {
            //Change the total and the payment values
            shoppingCar.setTotal(0.0);
            shoppingCar.setPay(0.0);
            //Change the quantity of the products available
            sendProductsUpdate(serviceList(shoppingCar.getId_shop()));
            //Delete all the items in the car
            shopXserviceService.deleteAllProducts(shoppingCar.getId_shop());
            return new ResponseEntity<>("Compra realizada", HttpStatus.OK);
        }
    }

    //Availability check
    private int checkAvailability(List<servicesShop> servicesShops, Integer quantity, Integer idProduct) {

        for (servicesShop auxSe : servicesShops) {

            if (auxSe.getId_service() == idProduct) {

                if (quantity <= auxSe.getAvailability()) {

                    return 1;
                }

            }
        }
        return -1;
    }

    //Total of items
    private Double getTotalShop(List<servicesShop> servicesShops, Integer idCar) {
        Double total = 0.0;
        for (shopxservice auxShop : shopXserviceService.getAllProducts(idCar)) {
            for (servicesShop auxServices : servicesShops) {
                if (auxShop.getId_producto() == auxServices.getId_service()) {
                    total = total + (auxServices.getPrice() * auxShop.getQuantiy());
                }
            }
        }
        return total;
    }

    //Make the list with the info of the services sold
    private List<String> serviceList(Integer idCar) {
        List<shopxservice> shopxservices = shopXserviceService.getAllProducts(idCar);
        List<String> allProductsSold = new ArrayList<>();
        for (shopxservice aux : shopxservices) {
            allProductsSold.add(new servicesShop(aux.getQuantiy(), 0.0, aux.getId_producto()).toString());
        }
        return allProductsSold;
    }

    //Send Products
    private void sendProductsUpdate(List<String> products) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(products, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8083/service/serviceSold", requestEntity, String.class);

    }


    //Bring Products
    @GetMapping("/getServices")
    private List<servicesShop> getServices() {
        List<servicesShop> respons = new ArrayList<>();

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8083/service/serviceShop";
        List<String> listaDeCadenas = null;
        try {
            listaDeCadenas = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<String>>() {
            }).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String auxS : listaDeCadenas) {
            String[] partes = auxS.split(",\\s*"); // Separar las partes del token
            String availability = partes[0].substring(partes[0].indexOf("=") + 1); // Obtener el valor de "availability"
            String price = partes[1].substring(partes[1].indexOf("=") + 1); // Obtener el valor de "price"
            String id_service = partes[2].substring(partes[2].indexOf("=") + 1); // Obtener el valor de "id_service"


            Integer auxAva = Integer.parseInt(availability);
            Integer auxId = Integer.parseInt(id_service);
            Double auxPrice = Double.parseDouble(price);


            respons.add(new servicesShop(auxAva, auxPrice, auxId));

        }

        return respons;
    }


}























