package com.example.giuliagigi.jobplacement;

/**
 * Created by MarcoEsposito90 on 10/05/2015.
 */
/* ----------------- EXCEPTION FOR HANDLING REGISTRATION ERRORS ------------------------------*/

public class RegistrationException extends Exception{

    public static final int MISSING_INFORMATIONS = 0;
    public static final int MISMATCHING_PASSWORDS = 1;

    private static final String MISSING_INFORMATIONS_MESSAGE = "Some important registration info is missing";
    private static final String MISMATCHING_PASSWORDS_MESSAGE = "Password and confirmPassword are not matching";

    private final String[] MESSAGES = new String[]{MISSING_INFORMATIONS_MESSAGE,MISMATCHING_PASSWORDS_MESSAGE};

    private int code;
    private String message;

    public RegistrationException(int code){
        super();

        this.code = code;
        this.message = MESSAGES[code];
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
