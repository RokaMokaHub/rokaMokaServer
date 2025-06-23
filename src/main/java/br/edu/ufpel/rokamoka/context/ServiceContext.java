package br.edu.ufpel.rokamoka.context;

import br.edu.ufpel.rokamoka.security.UserAuthenticated;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

/**
 * @author mauriciomucci
 */
@Data
@Slf4j
public class ServiceContext {

    private static final ThreadLocal<ServiceContext> threadLocal = new ThreadLocal<>();

    // MDC - Mapped Diagnostic Context
    private final static String USERNAME = "username";
    private final static String EXECUTION_UUID = "executionUUID";

    // for thread timing
    private final long startTime = System.currentTimeMillis();
    private long endTime;
    private long elapsedTime;

    // for request context
    private UserAuthenticated user;
    private HttpStatus httpStatus;
    private String executionUUID;
    private Exception exception;

    private ServiceContext(String executionUUID) {
        if (StringUtils.isBlank(executionUUID)) {
            executionUUID = java.util.UUID.randomUUID().toString();
        }

        this.executionUUID = executionUUID;
        this.httpStatus = HttpStatus.OK;

        MDC.put(EXECUTION_UUID, this.executionUUID);
        MDC.remove(USERNAME);
    }

    public static synchronized ServiceContext getContext() {
        ServiceContext ctx = threadLocal.get();
        if (ctx == null) {
            ctx = newContext();
            log.debug("Novo contexto: [{}]", ctx.executionUUID);
            return ctx;
        }

        log.debug("Retornando contexto: [{}]", ctx.executionUUID);
        return ctx;
    }

    public static ServiceContext newContext() {return newContext(java.util.UUID.randomUUID().toString());}

    private static ServiceContext newContext(String executionUUID) {
        ServiceContext ctx = new ServiceContext(executionUUID);
        threadLocal.set(ctx);
        return ctx;
    }

    public static synchronized void clearContext() {
        if (threadLocal.get() != null) {
            log.debug("Limpando contexto: [{}]", threadLocal.get().executionUUID);
        }
        threadLocal.remove();
        MDC.clear();
    }

    public void setUser(UserAuthenticated user) {
        this.user = user;
        MDC.put(USERNAME, user.getUsername());
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.elapsedTime = this.endTime - startTime;
    }
}
