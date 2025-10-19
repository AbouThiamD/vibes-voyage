package com.descodeuses.voyage.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.descodeuses.voyage.model.Video;

public interface VideoService {

    public Video saveVideo(Video video);

    public List<Video> getAllVideo();

    public Boolean deleteVideo(Long id);

    Video uploadVideo(MultipartFile file) throws IOException;

    Video getById(Long id);    

    boolean existsById(Long id);

    Path getVideoPath(String fileName);
    
    Video getVideoById(Long id);
}
