package com.descodeuses.voyage.form;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data

public class VideoForm {

    private String nomVideo;
    private String idcategorie;
    private String description;
    private Date date;
    private MultipartFile fichier;

}
