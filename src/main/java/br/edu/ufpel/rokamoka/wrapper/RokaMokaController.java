package br.edu.ufpel.rokamoka.wrapper;

import br.edu.ufpel.rokamoka.context.ApiResponseWrapper;
import br.edu.ufpel.rokamoka.context.ServiceContext;
import br.edu.ufpel.rokamoka.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mauriciomucci
 */
@Slf4j
@RestController
public abstract class RokaMokaController {

    private static final String EXECUTION_UUID = "Execution-UUID";
    private static final String EXECUTION_START_TIME = "Execution-Start-Time";
    private static final String EXECUTION_END_TIME = "Execution-End-Time";
    private static final String EXECUTION_ELAPSED_TIME = "Execution-Elapsed-Time";

    protected ResponseEntity<ApiResponseWrapper<Void>> success() {
        log.info("Sucesso ao executar APIResponse<Void>");

        ServiceContext ctx = ServiceContext.getContext();
        ctx.setEndTime(System.currentTimeMillis());

        HttpHeaders headers = buildResponseHeaders(ctx);
        ApiResponseWrapper<Void> body = new ApiResponseWrapper<>(ctx);

        ServiceContext.clearContext();
        return ResponseEntity.ok().headers(headers).body(body);
    }

    protected <T> ResponseEntity<ApiResponseWrapper<T>> success(T response) {
        log.info("Sucesso ao executar APIResponse<{}>", response.getClass().getSimpleName());

        ServiceContext ctx = ServiceContext.getContext();
        ctx.setEndTime(System.currentTimeMillis());

        HttpHeaders headers = buildResponseHeaders(ctx);
        ApiResponseWrapper<T> body = new ApiResponseWrapper<>(ctx, response);

        ServiceContext.clearContext();
        return ResponseEntity.ok().headers(headers).body(body);
    }

    protected ResponseEntity<ApiResponseWrapper<Void>> error(Exception e, HttpStatus status) {
        log.error("", e);

        ServiceContext ctx = ServiceContext.getContext();
        ctx.setException(e);
        ctx.setHttpStatus(status);
        ctx.setEndTime(System.currentTimeMillis());

        HttpHeaders headers = buildResponseHeaders(ctx);
        ApiResponseWrapper<Void> body = new ApiResponseWrapper<>(ctx);

        ServiceContext.clearContext();
        return ResponseEntity.status(status).headers(headers).body(body);
    }

    private HttpHeaders buildResponseHeaders(ServiceContext ctx) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Headers", String.join(", ",
                EXECUTION_UUID, EXECUTION_START_TIME, EXECUTION_END_TIME, EXECUTION_ELAPSED_TIME));
        headers.add(EXECUTION_UUID, ctx.getExecutionUUID());
        headers.add(EXECUTION_START_TIME, DateUtils.formatDateTimeMillis(ctx.getStartTime()));
        headers.add(EXECUTION_END_TIME, DateUtils.formatDateTimeMillis(ctx.getEndTime()));
        headers.add(EXECUTION_ELAPSED_TIME, ctx.getElapsedTime() + "ms");
        return headers;
    }
}
