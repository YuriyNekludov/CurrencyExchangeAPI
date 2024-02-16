package edu.project.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ScriptReader {

    private ScriptReader() {
    }

    static void initScripts() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            String scripts = readScriptFromFile("database_init.sql")
                    + readScriptFromFile("data_init.sql");
            executeQueries(scripts, statement);
        } catch (SQLException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static void initDeleteScript() {
        try (Connection connection = ConnectionManager.getConnection();
             Statement statement = connection.createStatement()) {
            String script = readScriptFromFile("clear_tables.sql");
            executeQueries(script, statement);
        } catch (SQLException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readScriptFromFile(String fileName) throws IOException, URISyntaxException {
        StringBuilder temp = new StringBuilder();
        Path path = Paths.get(ConnectionManager.class
                .getClassLoader().getResource("scripts/" + fileName).toURI()).toAbsolutePath();
        List<String> lines = Files.readAllLines(path);
        for (String line : lines)
            temp.append(line).append(System.lineSeparator());
        return temp.toString();
    }

    private static void executeQueries(String scripts, Statement statement) throws SQLException {
        String[] queries = scripts.split(";");
        for (String query : queries) {
            if (!query.trim().isEmpty())
                statement.addBatch(query);
        }
        statement.executeBatch();
    }
}
