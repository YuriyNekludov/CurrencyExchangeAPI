package edu.project.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.project.dto.CurrencyDto;
import edu.project.services.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService service;
    private ObjectMapper mapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        service = (CurrencyService) config.getServletContext().getAttribute("currencyService");
        mapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        String code = uri.substring("/currency/".length()).toUpperCase();
        CurrencyDto currency = service.getElementByCode(code);
        String jsonResponse = mapper.writeValueAsString(currency);
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
