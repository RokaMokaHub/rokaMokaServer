package br.edu.ufpel.rokamoka.dto;

import jakarta.validation.groups.Default;

/**
 * Interface para grupos de validação do Jakarta Bean Validation.
 * <p>
 * Define grupos específicos para diferentes cenários de validação, permitindo aplicar validações condicionais baseadas
 * no contexto.
 *
 * @author MauricioMucci
 */
public interface GroupValidators {

    /**
     * Grupo de validação para operações de criação. Usado quando um novo objeto está sendo criado.
     */
    interface Create extends Default {}

    /**
     * Grupo de validação para operações de atualização. Usado quando um objeto existente está sendo modificado.
     */
    interface Update extends Default {}
}
