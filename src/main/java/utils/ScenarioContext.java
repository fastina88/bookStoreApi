package utils;

import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioContext {

    private static ScenarioContext instance;

    private String bookId;
    private String authToken;
    private String existingUser;
    private String userPassword;
    private Response lastResponse;
    private String remember_me;

    private ScenarioContext() {
    }

    public static ScenarioContext getInstance() {
        if (instance == null) {
            instance = new ScenarioContext();
        }
        return instance;
    }

}
