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

// @Service
// public class VideoServiceImpl implements VideoService {
//     @Autowired
//     private VideoRepository videoRepository;

//     @Override
//     public Video saveVideo(Video video) {
//         return videoRepository.save(video);
//     }

//     @Override
//     public List<Video> getAllVideo() {
//         return videoRepository.findAll();
//     }

//     @Override
//     public Boolean deleteVideo(Long id) {
//         throw new UnsupportedOperationException("Not supported yet.");
//     }
// }

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
            // 1) supprimer le fichier physique si on a une URL/nom de fichier
            try {
                if (v.getUrl() != null && !v.getUrl().isBlank()) {
                    java.nio.file.Files.deleteIfExists(getVideoPath(v.getUrl()));
                }
            } catch (Exception ignore) {
                // on ignore l'erreur de fichier, on continue à supprimer en BDD
            }
            // 2) supprimer en base
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
}
