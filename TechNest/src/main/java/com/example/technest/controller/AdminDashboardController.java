package com.example.technest.controller;

import com.example.technest.constant.ConstantData;
import com.example.technest.dto.InventoryFormDto;
import com.example.technest.dto.ProductFormDto;
import com.example.technest.entity.Inventory;
import com.example.technest.entity.Product;
import com.example.technest.entity.Users;
import com.example.technest.entity.UsersDetails;
import com.example.technest.exception.ProductNotFoundException;
import com.example.technest.markerInterface.AddMode;
import com.example.technest.markerInterface.UpdateMode;
import com.example.technest.service.AdminService;
import com.example.technest.service.InventoryService;
import com.example.technest.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminDashboardController {
    @Autowired
    AdminService adminService;
    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/admin")
    public String adminPage(){
        return "Admin_DashBoard";
    }

    @GetMapping("/admin/users")
    public String getUsers(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Users> usersPage = adminService.getUsers(page, 10);
        model.addAttribute("usersPage", usersPage);
        return "Admin_User_List";
    }



    @GetMapping("/admin/users/view/{id}")
    public String viewUser(@PathVariable int id, Model model) {

        UsersDetails usersDetails = adminService.getUserDetailsByUsersId(id);
        model.addAttribute("userdetails", usersDetails);

        return "fragments/user_detail::userDetailContent";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable int id, RedirectAttributes redirectAttributes) {
        adminService.deleteUserAndDetails(id);
        redirectAttributes.addFlashAttribute("deleteSuccess", true);
        return "redirect:/admin/users";
    }



    // controller handler for admin products


        @GetMapping("/admin/products")
        public String showProducts(@RequestParam(value = "category", required = false) String categoryName, Model model)
        {
            int categoryId;

            if (categoryName == null || categoryName.equalsIgnoreCase("Television")) {
                categoryId = ConstantData.televisionID;
                model.addAttribute("selectedCategory", "Television");
            } else if (categoryName.equalsIgnoreCase("Refrigerator")) {
                categoryId = ConstantData.refrigeratorId;
                model.addAttribute("selectedCategory", "Refrigerator");
            } else if (categoryName.equalsIgnoreCase("Washing Machine")) {
                categoryId = ConstantData.washingMachineID;
                model.addAttribute("selectedCategory", "Washing Machine");
            } else if (categoryName.equalsIgnoreCase("Water Purifier")) {
                categoryId = ConstantData.waterPurifierID;
                model.addAttribute("selectedCategory", "Water Purifier");
            } else if (categoryName.equalsIgnoreCase("Geyser")) {
                categoryId = ConstantData.geyserID;
                model.addAttribute("selectedCategory", "Geyser");
            } else if (categoryName.equalsIgnoreCase("AC")) {
                categoryId = ConstantData.airConditionerID;
                model.addAttribute("selectedCategory", "AC");
            } else {
                // Fallback default
                categoryId = ConstantData.televisionID;
                model.addAttribute("selectedCategory", "Television");
            }

            //  Load products
            List<Product> products = productService.getProductsByCategoryId(categoryId);
            model.addAttribute("products", products);

            //  Only override if not coming from flash
            if (!model.containsAttribute("productFormDto")) {
                model.addAttribute("productFormDto", new ProductFormDto());
            }
            if (!model.containsAttribute("updateMode")) {
                model.addAttribute("updateMode", false);
            }
            if (!model.containsAttribute("showModal")) {
                model.addAttribute("showModal", false);
            }

            // 🎉 Success messages
            if (!model.containsAttribute("productAdded")) {
                model.addAttribute("productAdded", false);
            }
            if (!model.containsAttribute("productDeleted")) {
                model.addAttribute("productDeleted", false);
            }
            if (!model.containsAttribute("productUpdated")) {
                model.addAttribute("productUpdated", false);
            }




            return "admin_products";
        }


    @GetMapping("/admin/products/view/{id}")
    public String viewProduct(@PathVariable int id, Model model) {

        Product productDetails = productService.getProductDetailsByProductId(id);
        model.addAttribute("productdetails", productDetails);

        return "/fragments/product_detail::productDetailContent";
    }


    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes)
    {
      productService.deleteProductById(id);
      redirectAttributes.addFlashAttribute("productDeleted",true);
      return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String editProduct(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Product product = productService.getProductById(id); // Fetching existing product
        ProductFormDto dto = productService.convertToDto(product); // Mapping entity → DTO

        redirectAttributes.addFlashAttribute("productFormDto", dto);
        redirectAttributes.addFlashAttribute("updateMode", true); // flag for update
        redirectAttributes.addFlashAttribute("showModal", true);  // open modal

        return "redirect:/admin/products";
    }


    @PostMapping("/admin/products/update")
    public String handleProductUpdate(@Validated(UpdateMode.class) @ModelAttribute ProductFormDto dto,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model)
    {
        if (result.hasErrors())
        {

            for (FieldError error : result.getFieldErrors()) {
                System.out.println("Field: " + error.getField() + ", Message: " + error.getDefaultMessage());
            }



            model.addAttribute("productFormDto", dto);
            model.addAttribute("updateMode", true);
            model.addAttribute("showModal", true);

            // Reload list & selected category if sudden form close

            int categoryId = ConstantData.televisionID;
            List<Product> products = productService.getProductsByCategoryId(categoryId);
            model.addAttribute("products", products);
            model.addAttribute("selectedCategory", "Television");


            return "admin_products";
        }

        productService.updateProduct(dto); // your service logic

        redirectAttributes.addFlashAttribute("productUpdated", true);
        return "redirect:/admin/products";
    }


    @GetMapping("/admin/products/add/new")
    public String triggerAddProduct(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("productFormDto", new ProductFormDto());
        redirectAttributes.addFlashAttribute("updateMode", false);
        redirectAttributes.addFlashAttribute("showModal", true);
        return "redirect:/admin/products";
    }


    @PostMapping("/admin/products/add")
    public String handleAddProduct(@Validated(AddMode.class) @ModelAttribute ProductFormDto dto, BindingResult result, RedirectAttributes redirectAttributes, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("productFormDto", dto); // repopulate the form
            model.addAttribute("showModal", true);     // trigger modal reopening

            // Optionally re-load product list and selected category when you suddenly close form
            int categoryId = ConstantData.televisionID;
            List<Product> products = productService.getProductsByCategoryId(categoryId);
            model.addAttribute("products", products);
            model.addAttribute("selectedCategory", "Television");


            return "admin_products"; // return the same page so the modal can show errors
        }
        productService.saveProductWithInventory(dto);
        redirectAttributes.addFlashAttribute("productAdded",true);
        return "redirect:/admin/products";
    }



// controller handler for admin inventory



    @GetMapping("/admin/inventory")
    public String showInventory(@RequestParam(value = "category", required = false) String categoryName, Model model)
    {
        int categoryId;

        if (categoryName == null || categoryName.equalsIgnoreCase("Television")) {
            categoryId = ConstantData.televisionID;
            model.addAttribute("selectedCategory", "Television");
        } else if (categoryName.equalsIgnoreCase("Refrigerator")) {
            categoryId = ConstantData.refrigeratorId;
            model.addAttribute("selectedCategory", "Refrigerator");
        } else if (categoryName.equalsIgnoreCase("Washing Machine")) {
            categoryId = ConstantData.washingMachineID;
            model.addAttribute("selectedCategory", "Washing Machine");
        } else if (categoryName.equalsIgnoreCase("Water Purifier")) {
            categoryId = ConstantData.waterPurifierID;
            model.addAttribute("selectedCategory", "Water Purifier");
        } else if (categoryName.equalsIgnoreCase("Geyser")) {
            categoryId = ConstantData.geyserID;
            model.addAttribute("selectedCategory", "Geyser");
        } else if (categoryName.equalsIgnoreCase("AC")) {
            categoryId = ConstantData.airConditionerID;
            model.addAttribute("selectedCategory", "AC");
        } else {
            // Fallback default
            categoryId = ConstantData.televisionID;
            model.addAttribute("selectedCategory", "Television");
        }

        //  Load inventory
        List<Inventory> inventories = inventoryService.getInventoryByCategoryId(categoryId);
        model.addAttribute("inventories", inventories);

        //  Only override if not coming from flash
        if (!model.containsAttribute("inventoryFormDto")) {
            model.addAttribute("inventoryFormDto", new InventoryFormDto());
        }
        if (!model.containsAttribute("updateMode")) {
            model.addAttribute("updateMode", false);
        }
        if (!model.containsAttribute("showModal")) {
            model.addAttribute("showModal", false);
        }

        // 🎉 Success messages
        if (!model.containsAttribute("inventoryAdded")) {
            model.addAttribute("inventoryAdded", false);
        }
        if (!model.containsAttribute("inventoryDeleted")) {
            model.addAttribute("inventoryDeleted", false);
        }
        if (!model.containsAttribute("inventoryUpdated")) {
            model.addAttribute("inventoryUpdated", false);
        }

        return "admin_inventory";
    }


    @GetMapping("/admin/inventory/add")
    public String triggerAddInventory(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("inventoryFormDto", new InventoryFormDto());
        redirectAttributes.addFlashAttribute("updateMode", false);
        redirectAttributes.addFlashAttribute("showModal", true);
        return "redirect:/admin/inventory";
    }


    @PostMapping("/admin/inventory/add")
    public String handleAddInventory(@Valid @ModelAttribute InventoryFormDto dto, BindingResult result, RedirectAttributes redirectAttributes, Model model)
    {
        if (result.hasErrors())
        {
            model.addAttribute("inventoryFormDto", dto); // repopulate the form
            model.addAttribute("showModal", true);     // trigger modal reopening

            // Optionally re-load inventory list and selected category when you suddenly close form
            int categoryId = ConstantData.televisionID;
            List<Inventory> inventories = inventoryService.getInventoryByCategoryId(categoryId);
            model.addAttribute("inventories", inventories);
            model.addAttribute("selectedCategory", "Television");


            return "admin_inventory"; // return the same page so the modal can show errors
        }
        try
        {
            inventoryService.saveInventoryWithProduct(dto);
            redirectAttributes.addFlashAttribute("inventoryAdded",true);
            return "redirect:/admin/inventory";
        }
        catch (ProductNotFoundException ex)
        {
            // Add a field-level error for productCode
            result.rejectValue("productCode", null, ex.getMessage());

            model.addAttribute("inventoryFormDto", dto);
            model.addAttribute("showModal", true);
            model.addAttribute("updateMode", false);

            int categoryId = ConstantData.televisionID;
            List<Inventory> inventories = inventoryService.getInventoryByCategoryId(categoryId);
            model.addAttribute("inventories", inventories);
            model.addAttribute("selectedCategory", "Television");
            return "admin_inventory"; // return the same page so the modal can show errors

        }


    }


    @GetMapping("/admin/inventory/edit/{id}")
    public String editInventory(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Inventory inventory = inventoryService.getInventoryById(id); // Fetching existing inventory
        InventoryFormDto dto = inventoryService.convertToDto(inventory); // Mapping entity → DTO

        redirectAttributes.addFlashAttribute("inventoryFormDto", dto);
        redirectAttributes.addFlashAttribute("updateMode", true); // flag for update
        redirectAttributes.addFlashAttribute("showModal", true);  // open modal


        return "redirect:/admin/inventory";
    }


    @PostMapping("/admin/inventory/update")
    public String handleInventoryUpdate(@Valid @ModelAttribute InventoryFormDto dto,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model)
    {
        if (result.hasErrors())
        {


            result.getAllErrors().forEach(e -> System.out.println("❌ " + e));
            model.addAttribute("inventoryFormDto", dto);
            model.addAttribute("updateMode", true);
            model.addAttribute("showModal", true);

            // Reload list & selected category if sudden form close

            int categoryId = ConstantData.televisionID;
            List<Inventory> inventories = inventoryService.getInventoryByCategoryId(categoryId);
            model.addAttribute("inventories", inventories);
            model.addAttribute("selectedCategory", "Television");

            return "admin_inventory";
        }


        inventoryService.updateInventory(dto);
        redirectAttributes.addFlashAttribute("inventoryUpdated", true);
        return "redirect:/admin/inventory";
    }




    @GetMapping("/admin/inventory/delete/{id}")
    public String deleteInventory(@PathVariable int id, RedirectAttributes redirectAttributes)
    {
        inventoryService.deleteInventoryById(id);
        redirectAttributes.addFlashAttribute("inventoryDeleted",true);
        return "redirect:/admin/inventory";
    }



}





