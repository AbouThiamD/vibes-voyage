package com.descodeuses.voyage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.descodeuses.dto.RestAPIResponse;
import com.descodeuses.voyage.form.CategorieForm;
import com.descodeuses.voyage.service.CategorieService;

@RestController
public class CategorieController {

    @Autowired
    CategorieService categorieService;

    // @PostMapping("/api/categorie/add")
    // public Object addCategorie(@RequestBody CategorieForm categorieForm)
    // throws Exception {
    // System.out.println("nom categorie : " + categorieForm + "/" +
    // categorieForm.getNomCategorie());
    // if (!categorieService.exists(categorieForm.getNomCategorie())) {
    // categorieService.createCategorie(categorieForm.getRootName(),
    // categorieForm.getNomCategorie());
    // return ResponseEntity.ok(new RestAPIResponse(200, "thematique enregistree"));
    // } else
    // throw new Exception("Thematique existe deja");
    // }

    // @DeleteMapping("/api/categorie/delete/{idCategorie}")
    // public Object deleteCategorie(@PathVariable String idCategorie) throws
    // Exception {
    // boolean ok = categorieService.deleteCategorie(idCategorie);
    // if (ok)
    // return ResponseEntity.ok(new RestAPIResponse(200, "thematique supprimee"));
    // else
    // throw new Exception("Pb a la suppression ");
    // }

}
