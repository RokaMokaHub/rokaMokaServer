package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to validate that the annotated element represents a valid email address and is not blank.
 *
 * <p>This annotation ensures that the email:
 * <ul>
 *     <li>Is formatted correctly as an email address.</li>
 *     <li>Is not blank.</li>
 * </ul>
 *
 * <p>It is intended to be used on fields, method parameters, or record components.
 *
 * @author mauriciomucci
 * @see Email
 * @see NotBlank
 * @see Constraint
 * @see Retention
 * @see Target
 */
@Email(message = "O formato do e-mail é inválido, por favor, insira um e-mail válido (ex: apelido@provedor.com)")
@NotBlank(message = "O campo de e-mail não pode ficar em branco")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface EmailConstraint {

    String message() default "Email inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
