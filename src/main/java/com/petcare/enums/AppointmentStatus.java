package com.petcare.enums;

public enum AppointmentStatus {
	
    PENDIENTE, 
    CONFIRMADA, 
    CANCELADA, 
    COMPLETADA;

    public static final AppointmentStatus DEFAULT = PENDIENTE;
}