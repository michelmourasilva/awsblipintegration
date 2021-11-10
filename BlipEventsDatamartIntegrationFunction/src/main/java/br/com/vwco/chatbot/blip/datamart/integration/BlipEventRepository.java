package br.com.vwco.chatbot.blip.datamart.integration;

import java.io.IOException;
import java.util.List;

public interface BlipEventRepository {
    void save(List<BlipEvent> events) throws IOException, MsSqlEventRepository.SaveError;

    void deleteAll() throws SaveError;

    class SaveError extends Exception {
        public SaveError(String message) {
            super(message);
        }
    }
}
