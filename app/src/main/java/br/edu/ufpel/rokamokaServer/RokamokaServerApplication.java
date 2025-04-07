package br.edu.ufpel.rokamokaServer;

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
public class RokamokaServerApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    private static final String TIMEZONE = "America/Sao_Paulo";
    private static ApplicationContext ctx;

    public static void main(String[] args) {

        System.setProperty("user.timezone", TIMEZONE);
        TimeZone.setDefault(TimeZone.getTimeZone(System.getProperty("user.timezone")));
        String timezone = TimeZone.getDefault().getID();

        ctx = SpringApplication.run(RokamokaServerApplication.class, args);

        Environment env = ctx.getEnvironment();
        log.warn("""
                 
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                 \t\t\tApplication [{}] is running
                 Profile: \t{}
                 Timezone: \t[{}]
                 Server: \t[{}:{}]
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%""",
                env.getProperty("spring.application.name"),
                env.getActiveProfiles(),
                timezone,
                env.getProperty("server.address"),
                env.getProperty("server.port"));
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
                 
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                 \t\t\t!!! STOPPING APPLICATION !!!
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%""");
    }
}
