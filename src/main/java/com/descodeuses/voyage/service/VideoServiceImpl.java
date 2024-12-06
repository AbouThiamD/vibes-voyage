package com.descodeuses.voyage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.VideoRepository;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public List<Video> getAllVideo() {
        return videoRepository.findAll();
    }

    @Override
    public Boolean deleteVideo(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
