package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final RoleService roleService;

    @Autowired
    private final PasswordEncoder encoder;

    private AdminsController(UserService userService, RoleService roleService, PasswordEncoder encoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @GetMapping()
    private String getAllUsers(Model model, Principal principal) {

            User user = new User();
            model.addAttribute("newUser", user);
            model.addAttribute("allRoles", roleService.getAllRoles());
            model.addAttribute("allUsers", userService.getAllUsers());
            model.addAttribute("user", userService.getUserByName(principal.getName()));

        return "index";
    }

    @GetMapping("/{id}")
    private String view(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "index";
    }

    @PostMapping("/save")
    private String save(@ModelAttribute("user") User user,
                        @RequestParam("formOfRoles") ArrayList<String> form) {

        userService.saveUser(user, form);

        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    private String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("userOfEdit", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRoles());

        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    private String updateUser(@PathVariable Long id, @ModelAttribute("userOfEdit") User userOfEdit, @RequestParam("formOfRoles") ArrayList<String> form, Model model){
        userService.updateUser(String.valueOf(id), userOfEdit, form);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    private String delete(@PathVariable Long id){
        userService.delete(id);
        return "redirect:/admin";
    }

}
