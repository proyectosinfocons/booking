package com.arauzo.booking.model;

public class ResponseMessage {
    private int code;
    private String message;

    public ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getters y Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
