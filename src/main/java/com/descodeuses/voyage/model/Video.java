package com.descodeuses.voyage.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Video ne peux pas etre nul")
    private String nomVideo;

    @NotBlank(message = "Video ne peux pas etre nul")
    private String description;


    private Date date;

    private String Categorie;
    private String url;

    @OneToMany(mappedBy = "video")
    private List<Commentaire> commentaire;

    @OneToMany(mappedBy = "video")
    private List<Favoris> favoris;

    @ManyToOne
    private Utilisateur utilisateur;

    private String vid;
    

   
   
   
}
