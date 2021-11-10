package br.com.vwco.chatbot.blip.datamart.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class LocalApp {
    private static final Logger logger = LoggerFactory.getLogger(LocalApp.class);
    public static void main(String[] args) throws IOException, InterruptedException, MsSqlEventRepository.SaveError, SQLException {
        var configFile = args[0];
        var startDate = LocalDate.parse(args[1]);
        var endDate = LocalDate.parse(args[2]);

        logger.debug("Start date: " + startDate);
        logger.debug("End date: " + endDate);
        logger.info("End date: " + endDate);

        final var config = new Config(configFile);

        logger.debug(config.dbConnectionUrl);

        var interactor = new IntegrationInteractor(
                new BlipService(new BlipClient(HttpClient.newHttpClient(), config), new JsonObjectMapper()),
                new MsSqlEventRepository(DriverManager.getConnection(config.dbConnectionUrl))
        );

        interactor.execute(startDate, endDate);
    }
}
