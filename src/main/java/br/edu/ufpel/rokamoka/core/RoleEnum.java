package br.edu.ufpel.rokamoka.core;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMINISTRATOR("admin"), RESEARCHER("pesquisador"), CURATOR("curador"), USER("usuario");

    private final String description;

    RoleEnum(String description) {
        this.description = description;
    }
}
