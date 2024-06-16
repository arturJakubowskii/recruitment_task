package io.getint.recruitment_task.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

    private Properties properties;

    public ConfigurationLoader() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("configuration.properties")) {
            if (input == null) {
                System.out.println("Unable to find file configuration.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getSourceProjectKey() {
        return properties.getProperty("source.project.key");
    }

    public String getTargetProjectKey() {
        return properties.getProperty("target.project.key");
    }
}

