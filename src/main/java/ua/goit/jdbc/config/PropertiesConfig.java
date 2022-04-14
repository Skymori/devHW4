package ua.goit.jdbc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig {
    private final Properties properties;

    public PropertiesConfig(String fileName) {
        properties = new Properties();
        loadPropertiesFile(fileName);
    }

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public void loadPropertiesFile(String filName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filName)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
