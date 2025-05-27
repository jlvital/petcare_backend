package com.petcare.enums;

public enum AccountStatus {

    ACTIVA,
    BLOQUEADA,
    DESACTIVADA;

    public boolean isActive() {
        return this == ACTIVA;
    }
}