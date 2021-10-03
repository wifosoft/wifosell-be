package com.wifosell.zeus.payload.exception;


import jdk.jfr.SettingDefinition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldErrorDTO {

    private String field;

    private String message;

    public FieldErrorDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    //Getters are omitted.
}