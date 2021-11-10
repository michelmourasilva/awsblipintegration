package br.com.vwco.chatbot.blip.datamart.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public class MsSqlEventRepository implements BlipEventRepository {
    private static final Logger logger = LoggerFactory.getLogger(MsSqlEventRepository.class);

    private static final String BLIP_INSERT_EVENT_SQL = "" +
            "INSERT INTO event (" +
            "  storageDate, " +
            "  category, " +
            "  action, " +
            "  stateName, " +
            "  stateId, " +
            "  messageId, " +
            "  previousStateId, " +
            "  previousStateName" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final Connection connection;


    public MsSqlEventRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(List<BlipEvent> events) throws SaveError {
        // Create a variable for the connection string.

        try (var stmt = connection.prepareStatement(BLIP_INSERT_EVENT_SQL);) {
            for (BlipEvent event : events) {
                stmt.setString(1, event.storageDate.toString());
                stmt.setString(2, event.category);
                stmt.setString(3, event.action);
                stmt.setString(4, event.extras.get("#stateName"));
                stmt.setString(5, event.extras.get("#stateId"));
                stmt.setString(6, event.extras.get("#messageId"));
                stmt.setString(7, event.extras.get("#previousStateId"));
                stmt.setString(8, event.extras.get("#previousStateName"));

                var updateCount = stmt.executeUpdate();

                if (updateCount != 1) {
                    throw new SaveError("Error saving event, query result count <> 1");
                }
            }
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
            throw new SaveError(e.getMessage());
        }
    }

    @Override
    public void deleteAll() throws SaveError {
        try (var stmt = connection.prepareStatement("DELETE FROM event");) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SaveError("Error deleting events: " + e.getMessage());
        }
    }
}
