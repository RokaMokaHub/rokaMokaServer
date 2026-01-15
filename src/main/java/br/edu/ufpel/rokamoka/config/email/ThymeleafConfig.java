package br.edu.ufpel.rokamoka.config.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Primary;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Configuration class for Thymeleaf template engine.
 *
 * <p>
 * This class configures the template engine to use both the database template resolver and the default classpath
 * template resolver.
 * </p>
 *
 * @author Mauricio Carvalho
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ThymeleafConfig {

    /**
     * Creates a template resolver that loads templates from the classpath. This resolver is used as a fallback if a
     * template is not found in the database.
     *
     * @return the classpath template resolver
     */
    @Bean
    @Description("Thymeleaf template resolver serving HTML templates from classpath")
    public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
        log.info("Configuring classpath template resolver");
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setOrder(1);
        resolver.setCacheable(true);
        resolver.setCheckExistence(true);
        resolver.setSuffix(".html");
        resolver.setPrefix("templates/");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    /**
     * Creates the Spring template engine with both resolvers. The database resolver is checked first, and if a template
     * is not found, the classpath resolver is used as a fallback.
     *
     * @return the configured template engine
     */
    @Bean
    @Primary
    @Description("Thymeleaf template engine with database and classpath resolvers")
    public SpringTemplateEngine emailTemplateEngine() {
        log.info("Configuring Thymeleaf template engine");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addTemplateResolver(this.classLoaderTemplateResolver());
        return engine;
    }
}
