package br.edu.ufpel.rokamoka.utils.location;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation to validate that the annotated element represents a valid CEP.
 *
 * @author MauricioMucci
 * @see NotBlank
 * @see Pattern
 * @see Constraint
 * @see Retention
 * @see Target
 */
@NotBlank(message = "O campo de CEP não pode ficar em branco")
@Pattern(regexp = "\\d{8}", message = "O CEP deve seguir o formato XXXXXXXX")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface CepConstraint {

    String message() default "CEP inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
