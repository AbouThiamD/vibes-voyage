package com.descodeuses.voyage.controller;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.descodeuses.voyage.form.VideoForm;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.VideoService;

import jakarta.servlet.http.HttpServletRequest;

@Controller

public class VideoController {
    @Autowired
    VideoService videoService;

    @Autowired
    CategorieService categorieService;

    // @GetMapping("/Tableau")
    // public String tableaudebord(Model model) {
    //     model.addAttribute("videos", videoService.listVideos());
    //     return "tableaudebord";

    // }

    @GetMapping("/pageVideos")
    public String pageVideos() {
        return "pageVideos"; 
    }
    
    // @GetMapping("/video/new")
    // public String newVideo(Model model) {

    // model.addAttribute("video", new Video());
    // return "newVideo";
    // }

    // @PostMapping("/video")
    // public String saveVideo(@Valid @ModelAttribute("video") Video video,
    // BindingResult bindingResult){
    // if(bindingResult.hasErrors()){
    // return "newVideo";
    // }
    // videoRepository.save(video);
    // return"redirect:/Tableau";
    // }

    // @GetMapping("video/new")
    // public String videoadd(ModelMap map) {
    //     map.put("categorieDTO", categorieService.list()
    //             .stream()
    //             .map(categorie -> ConverterDTO.convertToDTO(categorie))
    //             .collect(Collectors.toList()));
    //     map.put("videoForm", new VideoForm());
    //     return "newVideo";
    // }

    @PostMapping("/private/video/add")
    public String videoSubmit(@ModelAttribute VideoForm videoForm,
            HttpServletRequest request) {
        MultipartFile _fichier = videoForm.getFichier();
        String nomFichier = System.currentTimeMillis() +
                _fichier.getOriginalFilename();
        try {
            IOUtils.copy(
                    _fichier.getInputStream(),
                    new FileOutputStream(new File(request.getServletContext().getRealPath("") +
                            nomFichier)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "redirect:/Tableau";

    }
}