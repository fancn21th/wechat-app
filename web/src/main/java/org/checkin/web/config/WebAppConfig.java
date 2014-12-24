package org.checkin.web.config;

import org.checkin.web.model.WebWechat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.MultipartConfigElement;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-16
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.checkin.web.controller"})
@PropertySources({
        @PropertySource("classpath:/file.properties"),
        @PropertySource("classpath:/web.wechat.properties")
})
public class WebAppConfig extends WebMvcConfigurerAdapter {

    private static final int _1M = 1024 * 1024;

    @Autowired
    private Environment env;

    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        String location = env.getRequiredProperty("file.location");
        int maxFileSize = env.getRequiredProperty("file.maxFileSize", Integer.class) * _1M;
        int maxRequestSize = env.getRequiredProperty("file.maxRequestSize", Integer.class) * _1M;
        int fileSizeThreshold = env.getRequiredProperty("file.fileSizeThreshold", Integer.class) * _1M;
        return new MultipartConfigElement(location, maxFileSize, maxRequestSize, fileSizeThreshold);
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.setCacheSeconds(0);
        interceptor.setUseExpiresHeader(true);
        interceptor.setUseCacheControlHeader(true);
        interceptor.setUseCacheControlNoStore(true);
        return interceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/libs/**").addResourceLocations("/libs/");
        registry.addResourceHandler("/app/**").addResourceLocations("/app/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webContentInterceptor());
    }

    @Bean
    public WebWechat wechat() {
        WebWechat webWechat = new WebWechat();
        webWechat.setRedirectUri(env.getRequiredProperty("web.wechat.redirect.uri"));
        webWechat.setComponentLoginPage(env.getRequiredProperty("web.wechat.componentloginpage"));
        webWechat.setOauth2Authorize(env.getRequiredProperty("web.wechat.oauth2.authorize"));
        webWechat.setAuthUri(env.getRequiredProperty("web.wechat.auth.uri"));
        webWechat.setDevMode(env.getRequiredProperty("web.wechat.dev.mode", Boolean.class));
        return webWechat;
    }
}
