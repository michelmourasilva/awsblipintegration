package br.com.vwco.chatbot.blip.datamart.integration;

import java.io.IOException;
import java.time.LocalDate;

public class IntegrationInteractor {
    private final BlipService blipService;
    private final BlipEventRepository blipEventRepository;

    public IntegrationInteractor(BlipService blipService, BlipEventRepository blipEventRepository) {
        this.blipService = blipService;
        this.blipEventRepository = blipEventRepository;
    }

    public void execute(LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException, MsSqlEventRepository.SaveError {
        final var blipEvents = blipService.fetchAllEvents(startDate, endDate);
        blipEventRepository.deleteAll();
        blipEventRepository.save(blipEvents);
    }
}
