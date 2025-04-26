package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mauriciomucci
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank
@Size(min = 8, max = 20)
public @interface PasswordConstraint {

    String message() default "Senha inválida: a senha deve ter entre 8 e 20 caracteres";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
