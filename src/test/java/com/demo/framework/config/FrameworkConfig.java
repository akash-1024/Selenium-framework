package com.demo.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class FrameworkConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = FrameworkConfig.class.getClassLoader()
                .getResourceAsStream("framework.properties")) {
            if (input == null) {
                throw new IllegalStateException("framework.properties was not found on the test classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private FrameworkConfig() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(toEnvironmentKey(key));
        }
        if (value == null || value.isBlank()) {
            value = PROPERTIES.getProperty(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing framework configuration: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    private static String toEnvironmentKey(String key) {
        return key.replace('.', '_').replace('-', '_').toUpperCase();
    }
}
