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

     @GetMapping("/home")
     public String home() {
     return "home";
    }

    @GetMapping("/accueil")
    public String accueil() {
        return "accueil";
    }

    @GetMapping("/destination")
    public String destination() {
        return "destination";
    }

    @GetMapping("/Afrique")
    public String Afrique() {
        return "Afrique";
    }

    @GetMapping("/playvideos")
    public String playvideos() {
        return "playvideos";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/amerique")
    public String amerique() {
        return "amerique";
    } 

 /*     */
   
    @GetMapping("/Tableau")
    public String tableaudebord(){
    return "tableaudebord";
    }

    @GetMapping("/asie")
    public String asie(){
        return "asie";
    }


    @GetMapping("/europe")
    public String europe(){
        return "europe";
    }


    @GetMapping("/oceanie")
    public String oceanie(){
        return "oceanie";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/error_page")
    public String error_page(){
        return "error_page";
    }


    @GetMapping("/formAdmin")
    public String formAdmin(){
        return "formAdmin";
    }


    @GetMapping("/formCategorie")
    public String formCategorie(){
        return "formCategorie";
    }

    @GetMapping("/formVideo")
    public String formVideo(){
        return "formVideo";
    }

    @GetMapping("/register_page")
    public String register_page(){
        return "register_page";
    }

    @GetMapping("/role")
    public String role(){
        return "role";
    }

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
