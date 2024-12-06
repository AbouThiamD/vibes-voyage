package com.descodeuses.voyage.controller;

// import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.descodeuses.voyage.model.Role;
// import com.descodeuses.voyage.service.RoleService;

@Controller
public class MainController {

    // private final RoleService roleService;

    // public MainController(RoleService roleService) {
    // this.roleService = roleService;
    // }

    // @GetMapping("/home")
    // public String registre() {
    // return "registre";
    // }

    @GetMapping("/card")
    public String card() {
        return "card";
    }

    @GetMapping("/destination")
    public String destination() {
        return "destination";
    }

    @GetMapping("/Afrique")
    public String Afrique() {
        return "Afrique";
    }

    @GetMapping("/PlayVideos")
    public String MesVideos() {
        return "MesVideos";
    }

    @GetMapping("/Contact")
    public String index() {
        return "index";
    }

    @GetMapping("/Amerique")
    public String amerique2() {
        return "amerique2";
    }

    // @GetMapping("/Tableau")
    // public String tableaudebord(){
    // return "tableaudebord";
    // }

    // @GetMapping("/role-list")
    // public String role(Model model) {
    // List<Role> roles = roleService.findAll();

    // model.addAttribute("roles", roles);

    // return "role";
    // }

    // @PostMapping("/add-role")
    // public String addRole(@RequestParam("nom") String nom) {
    // System.out.println("le nom saisi par l'utilisateur est: " + nom);
    // Role role = new Role();
    // role.setNom(nom);
    // roleService.save(role);
    // return "redirect:/role-list";
    // }
}
