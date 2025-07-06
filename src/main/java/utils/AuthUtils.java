package utils;

import clients.UserClient;
import io.restassured.response.Response;
import models.user;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthUtils {

    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);

    private AuthUtils() {
    }

    /**
     * Utility method to check if the service context has a token saved .
     * If no token present, login and get the token again.
     */
    public static Object ensureToken(ScenarioContext context) {
        if (context.getAuthToken() == null || context.getAuthToken().isEmpty()) {
            logger.warn("Auth token is missing. Logging in to generate new token...");
            final user data = user.getInstance();
            data.setEmail(ScenarioContext.getInstance().getExistingUser());
            data.setPassword(ScenarioContext.getInstance().getUserPassword());
            Response response = UserClient.loginUser(data, 200);
            String token = response.jsonPath().getString("accessToken");
            String rememberMe = response.getCookie("remember-me");

            data.setLoginResponse(response);
            data.setAccessToken(token);
            data.setRememberMeCookie(rememberMe);
            context.setAuthToken(token);
            context.setRemember_me(rememberMe);

            logger.info("New auth token generated" );
            return token;
        } else {
            logger.info("Auth token already present");
            return context.getAuthToken();
        }
     }


}
