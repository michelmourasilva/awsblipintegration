package br.com.vwco.chatbot.blip.datamart.integration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class Config {

    public final String blipKey;
    public final String blipUri;
    public final String dbConnectionUrl;

    public Config(String blipKey, String blipUri, String dbConnectionUrl) {
        this.blipKey = blipKey;
        this.blipUri = blipUri;
        this.dbConnectionUrl = dbConnectionUrl;
    }

    public Config(String configFilename) throws IOException {
        String fileName = configFilename != null && configFilename.length() > 0 ? configFilename : "config.properties";
        Properties properties = new Properties();

        FileInputStream inputStream = new FileInputStream(fileName);
        properties.load(inputStream);

        inputStream.close();

        this.blipKey = properties.getProperty("blipKey");
        this.blipUri = properties.getProperty("blipUri");
        dbConnectionUrl = properties.getProperty("dbConnectionUrl");
    }
}
