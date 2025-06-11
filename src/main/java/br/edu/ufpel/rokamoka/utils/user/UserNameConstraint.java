package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enforce specific constraints on a username.
 *
 * <p>This annotation ensures that the username:
 * <ul>
 *     <li>Contains only letters, numbers, hyphens, and underscores.</li>
 *     <li>Adheres to the pattern defined by the {@link Pattern} annotation.</li>
 * </ul>
 *
 * <p>It is typically used on fields, method parameters, or record components to validate the format automatically.
 *
 * @author mauriciomucci
 * @see Pattern
 * @see Constraint
 * @see Retention
 * @see Target
 */
@Pattern(regexp = "^[a-zA-Z0-9_-]+$",
        message = "Nome de usuário inválido - apenas letras, números, hífen e underline são permitidos")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface UserNameConstraint {

    String message() default "Nome de usuário inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
