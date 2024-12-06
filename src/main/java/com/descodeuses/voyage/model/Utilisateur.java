package com.descodeuses.voyage.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "utilisateur")
    private List<Commentaire> commentaire;

    @OneToMany(mappedBy = "utilisateur")
    private List<Favoris> favoris;

    @OneToMany(mappedBy = "utilisateur")
    private List<Video> video;
    private String username;
    private String pseudo;
    private String email;
    private String mdp;
}
