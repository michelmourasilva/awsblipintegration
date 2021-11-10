package br.com.vwco.chatbot.blip.datamart.integration;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;

public class BlipServiceImplTest {

    @Test
    public void fetchCategories() throws IOException, InterruptedException {

        final var blipClient = Mockito.mock(BlipClient.class);
        final var blipConfig = Mockito.mock(BlipConfig.class);

        Mockito.when(blipClient.fetchCategories(LocalDate.now(), LocalDate.now())).thenReturn(
                "{" +
                        "    \"type\": \"application/vnd.lime.collection+json\"," +
                        "    \"resource\": {" +
                        "        \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                        "        \"items\": [" +
                        "            {" +
                        "                \"category\": \"accounts\"" +
                        "            }," +
                        "            {" +
                        "                \"category\": \"payments\"" +
                        "            }" +
                        "        ]" +
                        "    }," +
                        "    \"method\": \"get\"," +
                        "    \"status\": \"success\"," +
                        "    \"id\": \"3\"," +
                        "    \"from\": \"postmaster@analytics.msging.net/#az-iris5\"," +
                        "    \"to\": \"contact@msging.net\"," +
                        "    \"metadata\": {" +
                        "        \"#command.uri\": \"lime://contact@msging.net/event-track\"" +
                        "    }" +
                        "}"
        );
        var blipService = new BlipService(
                blipClient, JsonMapper.builder()
                .findAndAddModules()
                .build());

        var categories = blipService.fetchCategories(LocalDate.now(), LocalDate.now());

        Assert.assertEquals(categories, Arrays.asList("accounts", "payments"));
    }

    @Test
    public void fetchActionsByCategory() throws IOException, InterruptedException {

        final var blipClient = Mockito.mock(BlipClient.class);
        final var blipConfig = Mockito.mock(BlipConfig.class);

        Mockito.when(blipClient.fetchActionsByCategory("payments", LocalDate.now(), LocalDate.now())).thenReturn("{" +
                    "\"type\": \"application/vnd.lime.collection+json\"," +
                    "\"resource\": {" +
                    "  \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                    "  \"items\": [" +
                    "    {" +
                    "      \"storageDate\": \"2019-06-25T03:00:00.000Z\"," +
                    "      \"category\": \"payments\"," +
                    "      \"action\": \"success-order\"," +
                    "      \"count\": \"4\"" +
                    "    }," +
                    "    {" +
                    "      \"storageDate\": \"2019-06-24T03:00:00.000Z\"," +
                    "      \"category\": \"payments\"," +
                    "      \"action\": \"failed-order\"," +
                    "      \"count\": \"1\"" +
                    "    }" +
                    "  ]" +
                    "}" +
                "}"
        );
        var blipService = new BlipService(
                blipClient, JsonMapper.builder()
                .findAndAddModules()
                .build());

        var actions = blipService.fetchActionsByCategory("payments", LocalDate.now(), LocalDate.now());

        Assert.assertEquals(actions, Arrays.asList("success-order", "failed-order"));
    }

    @Test
    public void fetchEventsByAction() throws IOException, InterruptedException {

        final var blipClient = Mockito.mock(BlipClient.class);
        final var blipConfig = Mockito.mock(BlipConfig.class);

        Mockito.when(blipClient.fetchEvents("flow", "Início", LocalDate.now(), LocalDate.now())).thenReturn("{" +
                "    \"type\": \"application/vnd.lime.collection+json\"," +
                "    \"resource\": {" +
                "        \"total\": 5," +
                "        \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                "        \"items\": [" +
                "            {" +
                "                \"ownerIdentity\": \"vwcov2@msging.net\"," +
                "                \"storageDate\": \"2021-07-20T21:51:06.610Z\"," +
                "                \"category\": \"flow\"," +
                "                \"action\": \"Início\"," +
                "                \"extras\": {" +
                "                    \"stateId\": \"onboarding\"," +
                "                    \"#stateName\": \"Início\"," +
                "                    \"#stateId\": \"onboarding\"," +
                "                    \"#messageId\": \"d09e2778-b450-4386-8cf4-2573162c1695\"" +
                "                }" +
                "            }," +
                "            {" +
                "                \"ownerIdentity\": \"vwcov2@msging.net\"," +
                "                \"storageDate\": \"2021-07-21T19:31:54.890Z\"," +
                "                \"category\": \"flow\"," +
                "                \"action\": \"Início\"," +
                "                \"extras\": {" +
                "                    \"stateId\": \"onboarding\"," +
                "                    \"#stateName\": \"Início\"," +
                "                    \"#stateId\": \"onboarding\"," +
                "                    \"#messageId\": \"64287d67-43c3-476b-a8d5-64c9d6637e16\"" +
                "                }" +
                "            }" +
                "        ]" +
                "    }," +
                "    \"method\": \"get\"," +
                "    \"status\": \"success\"," +
                "    \"id\": \"5\"," +
                "    \"from\": \"postmaster@analytics.msging.net/#az-iris2\"," +
                "    \"to\": \"contact@msging.net\"," +
                "    \"metadata\": {" +
                "        \"#command.uri\": \"lime://contact@msging.net/event-track/payments/success-order?startDate=2019-06-21&endDate=2019-06-28&$take=10\"" +
                "    }" +
                "}"
        );
        var blipService = new BlipService(
                blipClient, JsonMapper.builder()
                .findAndAddModules()
                .build());

        var actions = blipService.fetchEvents("flow", "Início", LocalDate.now(), LocalDate.now());

        Assert.assertEquals(actions, Arrays.asList(
                new BlipEvent(
                        "vwcov2@msging.net",
                        Instant.parse("2021-07-20T21:51:06.610Z"),
                        "flow",
                        "Início",
                        new HashMap<String, String>() {{
                            put("stateId", "onboarding");
                            put("#stateName", "Início");
                            put("#stateId", "onboarding");
                            put("#messageId", "d09e2778-b450-4386-8cf4-2573162c1695");
                        }}
                ),
                new BlipEvent(
                        "vwcov2@msging.net",
                        Instant.parse("2021-07-21T19:31:54.890Z"),
                        "flow",
                        "Início",
                        new HashMap<String, String>() {{
                            put("stateId", "onboarding");
                            put("#stateName", "Início");
                            put("#stateId", "onboarding");
                            put("#messageId", "64287d67-43c3-476b-a8d5-64c9d6637e16");
                        }}
                )
        ));
    }

    @Test
    public void fetchAllEvents() throws IOException, InterruptedException {

        final var blipClient = Mockito.mock(BlipClient.class);
        final var blipConfig = Mockito.mock(BlipConfig.class);

        Mockito.when(blipClient.fetchCategories(LocalDate.now(), LocalDate.now())).thenReturn(
                "{" +
                        "    \"type\": \"application/vnd.lime.collection+json\"," +
                        "    \"resource\": {" +
                        "        \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                        "        \"items\": [" +
                        "            {" +
                        "                \"category\": \"accounts\"" +
                        "            }," +
                        "            {" +
                        "                \"category\": \"payments\"" +
                        "            }" +
                        "        ]" +
                        "    }," +
                        "    \"method\": \"get\"," +
                        "    \"status\": \"success\"," +
                        "    \"id\": \"3\"," +
                        "    \"from\": \"postmaster@analytics.msging.net/#az-iris5\"," +
                        "    \"to\": \"contact@msging.net\"," +
                        "    \"metadata\": {" +
                        "        \"#command.uri\": \"lime://contact@msging.net/event-track\"" +
                        "    }" +
                        "}"
        );
        Mockito.when(blipClient.fetchActionsByCategory("accounts", LocalDate.now(), LocalDate.now())).thenReturn("{" +
                "\"type\": \"application/vnd.lime.collection+json\"," +
                "\"resource\": {" +
                "  \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                "  \"items\": [" +
                "  ]" +
                "}" +
                "}"
        );
        Mockito.when(blipClient.fetchActionsByCategory("payments", LocalDate.now(), LocalDate.now())).thenReturn("{" +
                "\"type\": \"application/vnd.lime.collection+json\"," +
                "\"resource\": {" +
                "  \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                "  \"items\": [" +
                "    {" +
                "      \"storageDate\": \"2019-06-25T03:00:00.000Z\"," +
                "      \"category\": \"payments\"," +
                "      \"action\": \"success-order\"," +
                "      \"count\": \"4\"" +
                "    }," +
                "    {" +
                "      \"storageDate\": \"2019-06-24T03:00:00.000Z\"," +
                "      \"category\": \"payments\"," +
                "      \"action\": \"failed-order\"," +
                "      \"count\": \"1\"" +
                "    }" +
                "  ]" +
                "}" +
                "}"
        );
        Mockito.when(blipClient.fetchEvents("payments", "success-order", LocalDate.now(), LocalDate.now())).thenReturn("{" +
                "    \"type\": \"application/vnd.lime.collection+json\"," +
                "    \"resource\": {" +
                "        \"total\": 5," +
                "        \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                "        \"items\": [" +
                "            {" +
                "                \"ownerIdentity\": \"vwcov2@msging.net\"," +
                "                \"storageDate\": \"2021-07-20T21:51:06.610Z\"," +
                "                \"category\": \"flow\"," +
                "                \"action\": \"Início\"," +
                "                \"extras\": {" +
                "                    \"stateId\": \"onboarding\"," +
                "                    \"#stateName\": \"Início\"," +
                "                    \"#stateId\": \"onboarding\"," +
                "                    \"#messageId\": \"d09e2778-b450-4386-8cf4-2573162c1695\"" +
                "                }" +
                "            }," +
                "            {" +
                "                \"ownerIdentity\": \"vwcov2@msging.net\"," +
                "                \"storageDate\": \"2021-07-21T19:31:54.890Z\"," +
                "                \"category\": \"flow\"," +
                "                \"action\": \"Início\"," +
                "                \"extras\": {" +
                "                    \"stateId\": \"onboarding\"," +
                "                    \"#stateName\": \"Início\"," +
                "                    \"#stateId\": \"onboarding\"," +
                "                    \"#messageId\": \"64287d67-43c3-476b-a8d5-64c9d6637e16\"" +
                "                }" +
                "            }" +
                "        ]" +
                "    }," +
                "    \"method\": \"get\"," +
                "    \"status\": \"success\"," +
                "    \"id\": \"5\"," +
                "    \"from\": \"postmaster@analytics.msging.net/#az-iris2\"," +
                "    \"to\": \"contact@msging.net\"," +
                "    \"metadata\": {" +
                "        \"#command.uri\": \"lime://contact@msging.net/event-track/payments/success-order?startDate=2019-06-21&endDate=2019-06-28&$take=10\"" +
                "    }" +
                "}"
        );
        Mockito.when(blipClient.fetchEvents("payments", "failed-order", LocalDate.now(), LocalDate.now())).thenReturn("{" +
                "    \"type\": \"application/vnd.lime.collection+json\"," +
                "    \"resource\": {" +
                "        \"total\": 5," +
                "        \"itemType\": \"application/vnd.iris.eventTrack+json\"," +
                "        \"items\": [" +
                "            {" +
                "                \"ownerIdentity\": \"vwcov2@msging.net\"," +
                "                \"storageDate\": \"2021-07-20T21:51:06.610Z\"," +
                "                \"category\": \"flow\"," +
                "                \"action\": \"Início\"," +
                "                \"extras\": {" +
                "                    \"stateId\": \"onboarding\"," +
                "                    \"#stateName\": \"Início\"," +
                "                    \"#stateId\": \"onboarding\"," +
                "                    \"#messageId\": \"d09e2778-b450-4386-8cf4-2573162c1695\"" +
                "                }" +
                "            }," +
                "            {" +
                "                \"ownerIdentity\": \"vwcov2@msging.net\"," +
                "                \"storageDate\": \"2021-07-21T19:31:54.890Z\"," +
                "                \"category\": \"flow\"," +
                "                \"action\": \"Início\"," +
                "                \"extras\": {" +
                "                    \"stateId\": \"onboarding\"," +
                "                    \"#stateName\": \"Início\"," +
                "                    \"#stateId\": \"onboarding\"," +
                "                    \"#messageId\": \"64287d67-43c3-476b-a8d5-64c9d6637e16\"" +
                "                }" +
                "            }" +
                "        ]" +
                "    }," +
                "    \"method\": \"get\"," +
                "    \"status\": \"success\"," +
                "    \"id\": \"5\"," +
                "    \"from\": \"postmaster@analytics.msging.net/#az-iris2\"," +
                "    \"to\": \"contact@msging.net\"," +
                "    \"metadata\": {" +
                "        \"#command.uri\": \"lime://contact@msging.net/event-track/payments/success-order?startDate=2019-06-21&endDate=2019-06-28&$take=10\"" +
                "    }" +
                "}"
        );
        var blipService = new BlipService(
                blipClient, JsonMapper.builder()
                .findAndAddModules()
                .build());

        var actions = blipService.fetchAllEvents(LocalDate.now(), LocalDate.now());

        Assert.assertEquals(actions, Arrays.asList(
                new BlipEvent(
                        "vwcov2@msging.net",
                        Instant.parse("2021-07-20T21:51:06.610Z"),
                        "flow",
                        "Início",
                        new HashMap<String, String>() {{
                            put("stateId", "onboarding");
                            put("#stateName", "Início");
                            put("#stateId", "onboarding");
                            put("#messageId", "d09e2778-b450-4386-8cf4-2573162c1695");
                        }}
                ),
                new BlipEvent(
                        "vwcov2@msging.net",
                        Instant.parse("2021-07-21T19:31:54.890Z"),
                        "flow",
                        "Início",
                        new HashMap<String, String>() {{
                            put("stateId", "onboarding");
                            put("#stateName", "Início");
                            put("#stateId", "onboarding");
                            put("#messageId", "64287d67-43c3-476b-a8d5-64c9d6637e16");
                        }}
                ),
                new BlipEvent(
                        "vwcov2@msging.net",
                        Instant.parse("2021-07-20T21:51:06.610Z"),
                        "flow",
                        "Início",
                        new HashMap<String, String>() {{
                            put("stateId", "onboarding");
                            put("#stateName", "Início");
                            put("#stateId", "onboarding");
                            put("#messageId", "d09e2778-b450-4386-8cf4-2573162c1695");
                        }}
                ),
                new BlipEvent(
                        "vwcov2@msging.net",
                        Instant.parse("2021-07-21T19:31:54.890Z"),
                        "flow",
                        "Início",
                        new HashMap<String, String>() {{
                            put("stateId", "onboarding");
                            put("#stateName", "Início");
                            put("#stateId", "onboarding");
                            put("#messageId", "64287d67-43c3-476b-a8d5-64c9d6637e16");
                        }}
                )
        ));
    }

}
