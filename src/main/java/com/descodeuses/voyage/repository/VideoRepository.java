package com.descodeuses.voyage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.descodeuses.voyage.model.Video;

@Repository
@RepositoryRestResource
public interface VideoRepository extends JpaRepository<Video, Long>  {
    List<Video> findByCategorie_NomCategorieIgnoreCase(String nomCategorie);
     List<Video> findByCategorie_Id(Long categorieId);

}
