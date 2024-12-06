package com.descodeuses.voyage.service;

import java.util.List;

import com.descodeuses.voyage.model.Video;

public interface VideoService {

    public Video saveVideo(Video video);
    public List<Video> getAllVideo();
    public Boolean deleteVideo (Long id);
    
    
    
}
