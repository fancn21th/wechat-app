package org.checkin.web.config;

import org.checkin.client.config.ClientConfig;
import org.checkin.datasource.config.DataSourceConfig;
import org.checkin.repository.config.RepositoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;
import java.nio.charset.StandardCharsets;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2014-12-16
 */
public class MessageWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Autowired
    MultipartConfigElement multipartConfigElement;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                DataSourceConfig.class,
                RepositoryConfig.class,
//                SecurityConfig.class,
                ClientConfig.class,
                AppConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebAppConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("dispatchOptionsRequest", "true");
        registration.setAsyncSupported(true);
        registration.setMultipartConfig(multipartConfigElement);
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
        return new Filter[]{characterEncodingFilter};
    }
}
