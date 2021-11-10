package br.com.vwco.chatbot.blip.datamart.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonObjectMapper extends ObjectMapper {

    public JsonObjectMapper() {
        super();
        registerModule(new JavaTimeModule());
    }
}
