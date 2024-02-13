package edu.project.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

public class ErrorHandler {
    private ErrorHandler() {
    }

    public static void handleException(HttpServletResponse resp, Throwable throwable) throws ServletException, IOException {
        int statusCode = determineStatusCode(throwable);
        String exceptionMessage = determineExceptionMessage(throwable);
        String jsonResponse = createJsonResponse(exceptionMessage);
        resp.setStatus(statusCode);
        resp.getWriter().write(jsonResponse);
    }

    private static int determineStatusCode(Throwable throwable) {
        return switch (throwable.getClass().getSimpleName()) {
            case "NoValidParametersException" -> HttpServletResponse.SC_BAD_REQUEST;
            case "DataAlreadyExistException" -> HttpServletResponse.SC_CONFLICT;
            case "InternalServerException" -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            case "DataNotFoundException" -> HttpServletResponse.SC_NOT_FOUND;
            default -> 0;
        };
    }

    private static String determineExceptionMessage(Throwable throwable) {
            return throwable.getMessage();
    }

    private static String createJsonResponse(String exceptionMessage) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(Collections.singletonMap("message", exceptionMessage));
        } catch (JsonProcessingException e) {
            return "\"message\": \"Ошибка сериализации JSON\"";
        }
    }
}
