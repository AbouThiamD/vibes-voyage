package com.descodeuses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Value("${video.upload.path}")
    private String videoStaticPath;

  @Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
    registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

    // Base disque à partir de la propriété video.upload.path (ex: upload/video)
    String base = java.nio.file.Paths.get(videoStaticPath).toAbsolutePath().toString();
    // Normalise + ajoute un slash final
    base = base.replace("\\", "/");
    if (!base.endsWith("/")) base += "/";

    // Ancien alias (admin) -> NE PAS CASSER
    registry.addResourceHandler("/files/**")
            .addResourceLocations("file:" + base)
            .setCachePeriod(3600);

    // Nouveau alias (front)
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + base)
            .setCachePeriod(3600);

    // (facultatif) log utile pour vérifier
    System.out.println("Serving /files/** and /uploads/** from: file:" + base);
}
}