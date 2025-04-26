package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mauriciomucci
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^[a-zA-Z0-9_-]+$")
public @interface UserNameConstraint {

    String message() default "Nome de usuário inválido: apenas letras, números, hífen e underline são permitidos";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
