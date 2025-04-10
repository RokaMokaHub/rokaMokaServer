package br.edu.ufpel.rokamoka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class RokaMokaApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    private static final String TIMEZONE = "America/Sao_Paulo";
    private static ApplicationContext ctx;

    public static void main(String[] args) {

        String property = "user.timezone";
        System.setProperty(property, TIMEZONE);
        String timeZone = System.getProperty(property);
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));

        ctx = SpringApplication.run(RokaMokaApplication.class, args);

        Environment env = ctx.getEnvironment();
        log.warn("""
                 
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                 \t\tApplication [{}] is running
                 Timezone: [{}]
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%""",
                env.getProperty("spring.application.name"), timeZone);
    }

    public static <T> T getBean(Class<T> clazz) {
        return ctx.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.warn("""
                 
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                 \t\t !!! STOPPING APPLICATION !!!!
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%""");
    }
}
