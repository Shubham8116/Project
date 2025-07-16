package com.example.technest.controller;

import com.example.technest.entity.Users;
import com.example.technest.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TechNestController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String showHome()
    {
     return "home";
    }

    @GetMapping("/login")
    public String showLoginPage()
    {
        return "login";
    }



    @GetMapping("/accessdenied")
    public String accessDenied(){
        return "accessdenied";
    }

    @GetMapping("/contact-us")
    public  String showContactInfo()
    {
        return "contact";
    }


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("users", new Users());

        return "signup";
    }

    @PostMapping("/register")
    public String processSignupForm(@Valid @ModelAttribute("users") Users users, BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        //System.out.println("0");
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                        System.out.println("Field: " + error.getField() + ", Error: " + error.getDefaultMessage());
            //System.out.println("1");

            });
            return "signup";
        }
        // Save user to the database
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        //users.setPassword("{noop}"+users.getPassword());

        usersService.save(users);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }

//    @GetMapping("/dashboard")
//    public String showDashboard()
//    {
//        return "Admin_DashBoard";
//    }

//    @GetMapping("/users")
//    public String showUsers()
//    {
//        return "Admin_User_List";
//    }

}
