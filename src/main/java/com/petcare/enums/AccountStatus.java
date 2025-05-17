package com.petcare.enums;

public enum AccountStatus {
	
    ACTIVADA, 
    BLOQUEADA, 
    DESACTIVADA;

    public boolean isActive() {
        return this == ACTIVADA;
    }
}