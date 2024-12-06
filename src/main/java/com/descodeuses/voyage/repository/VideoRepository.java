package com.descodeuses.voyage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.descodeuses.voyage.model.Video;

@Repository
@RepositoryRestResource
public interface VideoRepository extends JpaRepository<Video, Long>  {

}
