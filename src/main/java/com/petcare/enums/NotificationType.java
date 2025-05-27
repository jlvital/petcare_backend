package com.petcare.enums;

public enum NotificationType {
    EMAIL, 
    SMS, 
    WHATSAPP;
    
    public static final NotificationType DEFAULT = EMAIL;
}