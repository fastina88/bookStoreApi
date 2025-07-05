package utils;

import io.restassured.response.Response;
import org.testng.Assert;

import java.util.Random;

public class commonUtilMethods {
    private static final Random RANDOM = new Random();

    public static String generateRandomPhoneNumber() {
        int firstDigit = 7 + RANDOM.nextInt(3);
        long remainingDigits = 100000000L + (long)(RANDOM.nextDouble() * 899999999L);

        return firstDigit + String.valueOf(remainingDigits);
    }

    public static void assertMessageEquals(Response response, String expectedSubstring, String key) {
        String actualMessage = response.jsonPath().getString(key);

        Assert.assertNotNull(actualMessage, "Response message is null");
        Assert.assertEquals(actualMessage, expectedSubstring, "Actual is not as expected");
    }
}
