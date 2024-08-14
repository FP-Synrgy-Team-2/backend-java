package com.example.jangkau.dto.auth;


import com.fasterxml.jackson.annotation.JsonProperty;

public class QrisRequest {
    @JsonProperty("encryptedData")
    private String encryptedData;

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }
}
