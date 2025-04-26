package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mauriciomucci
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@Email
public @interface EmailConstraint {

    String message() default "Email inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
