package com.descodeuses.voyage.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.FetchType;

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

    @ManyToOne
    @JoinColumn(name = "categorie_id") 
    private Categorie categorie;

    @Column(name = "video") 
    private String url;

    @Column(name = "image")   
    private String image;

    
   @ManyToOne(fetch = FetchType.EAGER, optional = false)
   @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    private String vid;
    

   
   
   
}
