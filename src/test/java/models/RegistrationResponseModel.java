package models;

import lombok.Data;

@Data
public class RegistrationResponseModel {

    private String token;
    private String error;
    private int id;
}