package com.retailedge.configuration;

import com.retailedge.model.WebConfigProperties;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableAsync
//@EnableConfigurationProperties(HostValidationProperties.class)
public class CustomWebConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;


    @Autowired
    private WebConfigProperties webConfigProperties;


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("api", HandlerTypePredicate.forAnnotation(RestController.class));
    }



    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> erv = List.of(webConfigProperties.getCors().getAllowedOrigins());
        registry.addMapping("/**")
                .allowedOrigins("https://retail-edge.netlify.app")
                // .allowedOrigins(webConfigProperties.getCors().getAllowedOrigins())
                // .allowedMethods(webConfigProperties.getCors().getAllowedMethods())
                // .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return registry -> {
            ErrorPage errorPage = new ErrorPage(HttpStatus.NOT_FOUND, "/");
            registry.addErrorPages(errorPage);
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "file://" + environment.getProperty("asset.path") + "/byte-factory/uploads/";
        registry
                .addResourceHandler("/public/**")
                .addResourceLocations(path);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
