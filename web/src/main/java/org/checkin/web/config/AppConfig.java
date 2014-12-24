package org.checkin.web.config;

import org.checkin.repository.repository.tables.pojos.FitnessCheckin;
import org.checkin.web.vo.FitnessCheckinVo;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;

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
@ComponentScan(basePackages = {"org.checkin.web"}, excludeFilters = {
        @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION)
})
@EnableAspectJAutoProxy
public class AppConfig {
    @Bean
    public BeanMappingBuilder beanMappingBuilder() {
        return new BeanMappingBuilder() {
            protected void configure() {
                mapping(FitnessCheckin.class, FitnessCheckinVo.class);
            }
        };
    }

    @Bean
    public Mapper mapper() {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(beanMappingBuilder());
        return mapper;
    }
}
