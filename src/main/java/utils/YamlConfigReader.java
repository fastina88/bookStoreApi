package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.Map;

public class YamlConfigReader {

    private static final Map<String, Object> yamlData;
    private static final String environment;

    static {
        try {
            InputStream inputStream = YamlConfigReader.class.getClassLoader().getResourceAsStream("config/config.yaml");
            if (inputStream == null) {
                throw new RuntimeException("YAML file not found! ");
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            yamlData = mapper.readValue(inputStream, Map.class);
            environment = System.getProperty("env", (String) yamlData.get("environment"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML config", e);
        }
    }

    /**
     * Utility method to fetch any value from the YAML given the service name and key.
     * Example: getValue("bookstore", "baseUrl")
     */
    public static String getValue(String serviceName, String key) {
        Map<String, Object> envConfig = (Map<String, Object>) yamlData.get(environment.toUpperCase());
        if (envConfig == null) {
            throw new RuntimeException("No config found for environment: " + environment);
        }

        Map<String, String> serviceConfig = (Map<String, String>) envConfig.get(serviceName);
        if (serviceConfig == null) {
            throw new RuntimeException("No service named '" + serviceName + "' found in environment: " + environment);
        }

        String value = serviceConfig.get(key);
        if (value == null) {
            throw new RuntimeException("No key '" + key + "' found for service: " + serviceName);
        }

        return value;
    }
}
