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

    

}
