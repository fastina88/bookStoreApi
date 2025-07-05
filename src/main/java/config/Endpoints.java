package config;

import utils.YamlConfigReader;

public enum Endpoints {
    REGISTER_USER("user", "registerUser"),
    LOGIN_USER("user", "loginUser"),
    LOGIN_WITH_COOKIE("user", "loginWithCookie"),
    DELETE_USER("user", "deleteUser"),
    HEALTH_CHECK("user", "health"),
    BASE_URL_USER("user","baseUrl"),

    CREATE_BOOK("bookstore","createBook"),
    GET_ALL_BOOKS("bookstore", "getAllBooks"),
    GET_BOOK_BY_ID("bookstore","getBookById"),
    UPDATE_BOOKID("bookstore","updateBookId"),
    DELETE_BOOK("bookstore","deleteBook"),
    BASE_URL_BOOK("bookstore","baseUrl");


    private final String serviceName;
    private final String key;

    Endpoints(String serviceName, String key) {
        this.serviceName = serviceName;
        this.key = key;
    }

    public String getEndpoint() {
        return YamlConfigReader.getValue(serviceName, key);
    }

    public String getBaseUrl() {
        return YamlConfigReader.getValue(serviceName, "baseUrl");
    }
}
