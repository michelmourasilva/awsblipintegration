package br.com.vwco.chatbot.blip.datamart.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlipService {
    private final BlipClient blipClient;
    private final ObjectMapper objectMapper;

    BlipService(BlipClient blipClient, ObjectMapper objectMapper) {
        this.blipClient = blipClient;
        this.objectMapper = objectMapper;
    }

    public List<String> fetchCategories(LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {
        var body = blipClient.fetchCategories(startDate, endDate);
        return objectMapper.readTree(body).get("resource").get("items").findValuesAsText("category");
    }


    /**
     * Get actions on a given category, on a given interval
     *
     * @param category
     * @return
     * @throws IOException
     * @throws InterruptedException
     */

    public List<String> fetchActionsByCategory(
            String category,
            LocalDate startDate,
            LocalDate endDate
    ) throws IOException, InterruptedException {
        var body = blipClient.fetchActionsByCategory(category, startDate, endDate);
        System.out.println(body);


        return objectMapper.readTree(body)
                .get("resource")
                .get("items")
                .findValuesAsText("action")
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public List<BlipEvent> fetchEvents(
            String category,
            String action,
            LocalDate startDate,
            LocalDate endDate
    ) throws IOException, InterruptedException {
        var body = blipClient.fetchEvents(category, action, startDate, endDate);
        System.out.println("Category: " + category + "Action: " + action + ": " + body);

        final var itemsNode = objectMapper.readTree(body)
                .get("resource")
                .get("items");

        return objectMapper.convertValue(itemsNode, new TypeReference<>() {
        });


    }

    public List<BlipEvent> fetchAllEvents(LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {

        var result = new ArrayList<BlipEvent>();
        var categories = fetchCategories(startDate, endDate);

        for (String category : categories) {
            var actions = fetchActionsByCategory(category, startDate, endDate);

            for (String action : actions) {
                result.addAll(fetchEvents(category, action, startDate, endDate));
            }
        }

        return result;
    }
}
