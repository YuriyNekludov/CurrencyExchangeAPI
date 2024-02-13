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
            String currScript = readScriptFromFile("database_init.sql");
            String currDataScript = readScriptFromFile("data_init.sql");
            String[] queries = (currScript + currDataScript).split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty())
                    statement.addBatch(query);
            }
            statement.executeBatch();
        } catch (SQLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String readScriptFromFile(String fileName) throws URISyntaxException {
        StringBuilder temp = new StringBuilder();
        Path path = Paths.get(ConnectionManager.class
                .getClassLoader().getResource("scripts/" + fileName).toURI()).toAbsolutePath();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                temp.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return temp.toString();
    }
}
