package com.jtfu.config;


import org.springframework.context.annotation.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("com.jtfu")
public class FreeMakerConfig extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

    /*配置静态资源的处理*/
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }



    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
        registry.freeMarker().cache(false);
    }


    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver=new CommonsMultipartResolver();
        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }


    @Bean
    public FreeMarkerConfigExtend freeMarkerConfigurer() {
        FreeMarkerConfigExtend configurer = new FreeMarkerConfigExtend();
        configurer.setTemplateLoaderPath("/WEB-INF/page/");
        configurer.setDefaultEncoding("utf-8");
        Properties properties=new Properties();
        properties.setProperty("template_update_delay","0");
        properties.setProperty("datetime_format","yyyy-MM-dd");
        properties.setProperty("locale","zh_CN");
        properties.setProperty("number_format","#");
        configurer.setFreemarkerSettings(properties);
        Map map=new HashMap();

        configurer.setFreemarkerVariables(map);//为freemarker设置全局参数；
        return configurer;
    }
    @Bean
    public ViewResolver FreeMarkerViewResolver(){
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setSuffix(".html");
        // resolver.setExposeContextBeansAsAttributes(true);
        resolver.setContentType("text/html; charset=UTF-8");
       // resolver.setOrder(2);
        return  resolver;
    }


}