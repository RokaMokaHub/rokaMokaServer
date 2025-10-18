package br.edu.ufpel.rokamoka.utils.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enforce specific constraints on a family name.
 *
 * <p>This annotation ensures that the family name:
 * <ul>
 *     <li>Starts with a capital letter.</li>
 *     <li>Contains only letters.</li>
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
@NotNull(message = "Nome de familia não pode ser nulo")
@Pattern(regexp = "^[A-Z][a-zA-Z]*$",
        message = "Nome de família inválido - deve começar com letra maiúscula e conter apenas letras.")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface FamilyNameConstraint {

    String message() default "Nome de família inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
