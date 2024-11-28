package org.figuramc.figura.cosmetics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class BackendConfig {
    public static final String apiUrl;
    public static final String apiKey;

    static {
        try {
            Properties prop = new Properties();
            Path path = Path.of("cosmetic-backend-config.properties");
            path.toFile().createNewFile();
            prop.load(Files.newInputStream(path));
            prop.putIfAbsent("apiUrl", "");
            prop.putIfAbsent("apiKey", "");
            apiUrl = prop.getProperty("apiUrl").replaceFirst("/*$", "");
            apiKey = prop.getProperty("apiKey");
            prop.store(Files.newOutputStream(path), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
