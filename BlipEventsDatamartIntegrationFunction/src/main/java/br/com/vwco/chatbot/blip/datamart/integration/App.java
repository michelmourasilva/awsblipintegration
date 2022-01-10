package br.com.vwco.chatbot.blip.datamart.integration;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.regions.Region;

import java.net.http.HttpClient;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ObjectMapper objectMapper = new JsonObjectMapper();

    private final Region region = Region.US_EAST_1;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        var logger = context.getLogger();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);


        // Extract and parse request body
        var body = input.getBody();
        logger.log("Request body: " + body);

        IntegrationRequest parsedBody;

        try {
            parsedBody = objectMapper.readValue(body, IntegrationRequest.class);            
        } catch (JacksonException e) {
            logger.log("Error parsing request body: " + e.getMessage());
            return response.withStatusCode(400)
                    .withBody("{ \"code\": \"cannot_parse_request\" }");
        }

        // Connect to the DB
        MsSqlEventRepository dbConnection = null;

        try {            
            dbConnection = new MsSqlEventRepository(DriverManager.getConnection(System.getenv("DB_CONNECTION_URL")));
        } catch (SQLException e) {
            logger.log("Error connecting to DB: " + e.getMessage() + " - connectingString: " + System.getenv("DB_CONNECTION_URL") + " //");
            return response.withStatusCode(500)
                    .withBody("{ \"code\": \"db_connection_error\" , \"conexao\": System.getenv(\"DB_CONNECTION_URL\")}");
        }

        // Execute the processs
        final IntegrationInteractor integrationInteractor = new IntegrationInteractor(
                new BlipService(
                        new BlipClient(
                                httpClient,
                                new Config(
                                        System.getenv("BLIP_KEY"),
                                        System.getenv("BLIP_URI"),
                                        System.getenv("DB_CONNECTION_URL")
                                )
                        ),
                        objectMapper
                ),
                dbConnection
        );

        try {
            logger.log("startDate: " + parsedBody.startDate + " / endDate: " + parsedBody.endDate + " / blipkey: " + System.getenv("BLIP_KEY") + " / blipurI: " + System.getenv("BLIP_URI") + " / dbconection:  " + System.getenv("DB_CONNECTION_URL") );
            integrationInteractor.execute(parsedBody.startDate, parsedBody.endDate);

            return response
                    .withStatusCode(200)
                    .withBody("{}");
        } catch (Exception e) {
            logger.log("Error executing integration: " + e.getMessage());
            return response
                    .withStatusCode(500)
                    .withBody("{ \"code\": \"db_integration_error\", \"message\": \"" + e.getMessage() + "\" }  ");
        }


    }

}


class IntegrationRequest {
    public LocalDate startDate;
    public LocalDate endDate;

    public IntegrationRequest(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public IntegrationRequest(){}
}
