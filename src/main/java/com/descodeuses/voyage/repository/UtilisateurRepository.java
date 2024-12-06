package com.descodeuses.voyage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.descodeuses.voyage.model.Utilisateur;

@Repository
@RepositoryRestResource
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>  {

   Optional <Utilisateur> findByPseudoAndMdp(String pseudo, String mdp);

   Optional <Utilisateur> findFirstByPseudo(String pseudo);

}
