package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the annotated password must meet specific constraints.
 *
 * <p> This annotation ensures that the password:
 * <ul>
 *     <li>Is not blank.</li>
 *     <li>Has a length between 8 and 20 characters, inclusive.</li>
 * </ul>
 *
 *  <p> It is intended to be used on fields, method parameters, or record components.
 *
 * @author mauriciomucci
 * @see Size
 * @see NotBlank
 * @see Constraint
 * @see Retention
 * @see Target
 */
@Size(min = 8, max = 20, message = "Senha inválida - a senha deve ter entre 8 e 20 caracteres")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface PasswordConstraint {

    String message() default "Senha inválida";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
