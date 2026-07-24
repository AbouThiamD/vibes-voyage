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

    
    String base = java.nio.file.Paths.get(videoStaticPath).toAbsolutePath().toString();
 
    base = base.replace("\\", "/");
    if (!base.endsWith("/")) base += "/";

    registry.addResourceHandler("/files/**")
            .addResourceLocations("file:" + base)
            .setCachePeriod(3600);

    
    registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + base)
            .setCachePeriod(3600);

   
}
}