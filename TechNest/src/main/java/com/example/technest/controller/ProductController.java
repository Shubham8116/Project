package com.example.technest.controller;

import com.example.technest.constant.ConstantData;
import com.example.technest.entity.Product;
import com.example.technest.service.ProductService;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/television")
    public String getTelevisionProducts(Model model) {

        List<Product> products = productService.getProductsWithCalculatedPrice(ConstantData.televisionID);
        model.addAttribute("products", products);
        return "television";
    }


    @GetMapping("/filter-television")
    public String filterTelevision(@RequestParam(required = false) String manufacturer, Model model) {
        List<Product> products;

        if (manufacturer == null || manufacturer.isBlank()) {
            products = productService.getProductsWithCalculatedPrice(ConstantData.televisionID);;
        } else {
            products = productService.getProductsByManufacturerAndCategoryId(manufacturer, ConstantData.televisionID);
        }

        model.addAttribute("products", products);
        return "fragments/product_list :: productList";
    }




    @GetMapping("/refrigerator")
    public String getRefrigeratorProducts(Model model) {


        List<Product> products = productService.getProductsWithCalculatedPrice(ConstantData.refrigeratorId);

        model.addAttribute("products", products);
        return "refrigerator";
    }

    @GetMapping("/filter-refrigerator")
    public String filterRefrigerator(@RequestParam(required = false) String manufacturer, Model model) {
        List<Product> products;

        if (manufacturer == null || manufacturer.isBlank()) {
            products = productService.getProductsWithCalculatedPrice(ConstantData.refrigeratorId);;
        } else {
            products = productService.getProductsByManufacturerAndCategoryId(manufacturer, ConstantData.refrigeratorId);
        }

        model.addAttribute("products", products);
        return "fragments/product_list :: productList";
    }


    @GetMapping("/washing-machine")
    public String getWashingMachineProducts(Model model) {

        List<Product> products = productService.getProductsWithCalculatedPrice(ConstantData.washingMachineID);
        model.addAttribute("products", products);

        return "washing_machine";
    }


    @GetMapping("/filter-washing-machine")
    public String filterWashingMachine(@RequestParam(required = false) String manufacturer, Model model) {
        List<Product> products;

        if (manufacturer == null || manufacturer.isBlank()) {
            products = productService.getProductsWithCalculatedPrice(ConstantData.washingMachineID);;
        } else {
            products = productService.getProductsByManufacturerAndCategoryId(manufacturer, ConstantData.washingMachineID);
        }

        model.addAttribute("products", products);
        return "fragments/product_list :: productList";
    }



    @GetMapping("/geyser")
    public String getGeyserProducts(Model model) {
        List<Product> products = productService.getProductsWithCalculatedPrice(ConstantData.geyserID); // Assuming 4 is the category ID for geysers
        model.addAttribute("products", products);

        return "geyser";
    }


    @GetMapping("/filter-geyser")
    public String filterGeyser(@RequestParam(required = false) String manufacturer, Model model) {
        List<Product> products;

        if (manufacturer == null || manufacturer.isBlank()) {
            products = productService.getProductsWithCalculatedPrice(ConstantData.geyserID);;
        } else {
            products = productService.getProductsByManufacturerAndCategoryId(manufacturer, ConstantData.geyserID);
        }

        model.addAttribute("products", products);
        return "fragments/product_list :: productList";
    }



    @GetMapping("/air-conditioner")
    public String getAirConditionerProducts(Model model) {

        List<Product> products = productService.getProductsWithCalculatedPrice(ConstantData.airConditionerID);
        model.addAttribute("products", products);
        return "air_conditioner";
    }


    @GetMapping("/filter-air-conditioner")
    public String filterAirConditioner(@RequestParam(required = false) String manufacturer, Model model) {
        List<Product> products;

        if (manufacturer == null || manufacturer.isBlank()) {
            products = productService.getProductsWithCalculatedPrice(ConstantData.airConditionerID);;
        } else {
            products = productService.getProductsByManufacturerAndCategoryId(manufacturer, ConstantData.airConditionerID);
        }

        model.addAttribute("products", products);
        return "fragments/product_list :: productList";
    }



        @GetMapping("/purifier")
        public String showPurifierPage(Model model) {
            List<Product> products = productService.getProductsWithCalculatedPrice(ConstantData.waterPurifierID);
            model.addAttribute("products", products);
            return "water_purifier";
        }

        @GetMapping("/filter-purifier")
        public String filterPurifier(@RequestParam(required = false) String manufacturer, Model model) {
            List<Product> products;

            if (manufacturer == null || manufacturer.isBlank()) {
                products = productService.getProductsWithCalculatedPrice(ConstantData.waterPurifierID);;
            } else {
                products = productService.getProductsByManufacturerAndCategoryId(manufacturer, ConstantData.waterPurifierID);
            }

            model.addAttribute("products", products);
            return "fragments/product_list :: productList";
        }




}
