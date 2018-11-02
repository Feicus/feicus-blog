package mblog.config;

import mblog.web.filter.ServletLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;


@Configuration
public class MdcFilterConfig {
    @Bean
    public FilterRegistrationBean mdcFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(mdcFilter());
        registration.addUrlPatterns("/*");
        registration.setName("mdcFilter");
        registration.setOrder(-1);
        return registration;
    }

    /**
     * 创建一个bean
     * @return
     */
    @Bean(name = "mdcFilter")
    public Filter mdcFilter() {
        return new ServletLogFilter();
    }

}
