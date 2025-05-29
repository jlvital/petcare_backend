package com.petcare.enums;

public enum BookingStatus {
    
	ANULADA, CANCELADA, COMPLETADA, CONFIRMADA;
   
    public static final BookingStatus DEFAULT = CONFIRMADA;
}