package models;

import io.restassured.response.Response;
import lombok.Data;
import lombok.Getter;

@Data
public class user {
    @Getter
    private static user instance = new user();

    private String phoneNumber;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String userId;
    private String accessToken;
    private String rememberMeCookie;

    private Response signUpResponse;
    private Response loginResponse;
    private Response editUserResponse;
    private Response deleteUserResponse;

    public user() {}

    public user(String fname, String lname, String number) {
        this.firstName = fname;
        this.lastName = lname;
        this.phoneNumber = number;
    }
}
