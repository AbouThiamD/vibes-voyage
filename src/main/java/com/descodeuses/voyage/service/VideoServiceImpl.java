package com.descodeuses.voyage.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.descodeuses.voyage.model.Video;
import com.descodeuses.voyage.repository.VideoRepository;


@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Value("${videos.static-path}")
    private String storagePath;

    private final VideoRepository repository;

    public VideoServiceImpl(VideoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Path getVideoPath(String nomVideo) {
        return Path.of(storagePath).resolve(nomVideo);
    }

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
        return videoRepository.findById(id).map(v -> {
            
            try {
                if (v.getUrl() != null && !v.getUrl().isBlank()) {
                    java.nio.file.Files.deleteIfExists(getVideoPath(v.getUrl()));
                }
            } catch (Exception ignore) {
               
            }
           
            videoRepository.deleteById(id);
            return true;
        }).orElse(false);
    }
         @Override
        public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video introuvable: " + id));

    }
        
         @Override
         public Video uploadVideo(MultipartFile file) throws IOException {
        throw new UnsupportedOperationException("Unimplemented method 'uploadVideo'");
    }
    @Override
public Video getById(Long id) {
    return videoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Vidéo introuvable id=" + id));
}

@Override
public boolean existsById(Long id) {
    return videoRepository.existsById(id);
}

}
