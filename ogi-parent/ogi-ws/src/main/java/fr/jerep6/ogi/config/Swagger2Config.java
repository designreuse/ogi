package fr.jerep6.ogi.config;

import java.time.LocalDate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@Configuration
public class Swagger2Config extends WebMvcConfigurerAdapter {

	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

  @Bean
  public Docket petApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build()
        .pathMapping("/rest")
        .directModelSubstitute(LocalDate.class, String.class)
        .useDefaultResponseMessages(false)
        .enableUrlTemplating(false);
  }

  @Bean
  UiConfiguration uiConfig() {
    return new UiConfiguration(
        "validatorUrl",// url
        "list",       // docExpansion          => none | list
        "alpha",      // apiSorter             => alpha
        "schema",     // defaultModelRendering => schema
        true,        // enableJsonEditor      => true | false
        true);        // showRequestHeaders    => true | false
  }
}