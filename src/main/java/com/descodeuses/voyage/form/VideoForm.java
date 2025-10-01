package com.descodeuses.voyage.form;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import com.descodeuses.voyage.model.Categorie;

import lombok.Data;

@Data

public class VideoForm {

    private String nomVideo;
    private String idcategorie;
    private Categorie categorie;
    private Long categorieId;  
    private String description;
    private Date date;
    private MultipartFile fichier;

}
