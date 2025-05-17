package com.petcare.enums;

public enum EmailTemplate {
	
    WELCOME_CLIENT("welcome_client"),
    WELCOME_EMPLOYEE("welcome_employee"),
    PASSWORD_RECOVERY("password_recovery"),
    ACCOUNT_DEACTIVATION("account_deactivation"),
    APPOINTMENT_REMINDER("appointment_reminder");

    private final String fileName;

    EmailTemplate(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}